package xyz.z3ro.colorenhance.utility

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ShellUtils
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileInputStream
import com.topjohnwu.superuser.io.SuFileOutputStream
import java.io.ByteArrayInputStream
import java.io.IOException

object Root {

    val rootAccess: Boolean
        get() = Shell.rootAccess()

    private fun restoreSingleFile(inputFileAbsolutePath: String, outputFileAbsolutePath: String): Boolean {
        val inputFile = SuFile(inputFileAbsolutePath)
        val outputFile = SuFile(outputFileAbsolutePath)

        val inputStream = SuFileInputStream(inputFile)
        val outputStream = SuFileOutputStream(outputFile)

        var returnValue = true

        if (inputFile.exists()) {
            try {
                ShellUtils.pump(inputStream, outputStream)
            } catch (e: IOException) {
                e.printStackTrace()
                returnValue = false
            }
        }
        return returnValue
    }

    fun restoreMultipleFiles(inputFilesAbsolutePath: List<String>, outputFilesAbsolutePath: List<String>): Boolean {
        var success = false

        for (i in inputFilesAbsolutePath.indices) {
            success = restoreSingleFile(inputFilesAbsolutePath[i], outputFilesAbsolutePath[i])
            if (!success) break
        }

        return success
    }

    fun writeToSingleFile(content: String, file: String): Boolean {
        val inputStream = ByteArrayInputStream(content.toByteArray(Charsets.UTF_8))
        val outputStream = SuFileOutputStream(SuFile(file))

        var success = true

        try {
            ShellUtils.pump(inputStream, outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            success = false
        }

        return success
    }

    fun writeToFiles(contents: List<String>, files: List<String>): Boolean {
        var success = false

        for (i in contents.indices) {
            success = writeToSingleFile(contents[i], files[i])
            if (!success) break
        }

        return success
    }

    private fun readContents(file: String): List<String>? {
        return Shell.su("cat $file").exec().out
    }

    fun readOneLine(file: String): String {
        val output = readContents(file)
        return if (output == null || output.isEmpty()) "" else output[0]
    }

    fun doesFileExist(file: String): Boolean {
        return SuFile(file).exists()
//        val output = Shell.su("ls $file").exec().out
//        return output != null && !output.isEmpty() && output[0] == file
    }
}