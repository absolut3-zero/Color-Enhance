package xyz.z3ro.colorenhance.utility

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ShellUtils
import com.topjohnwu.superuser.io.SuFile
import com.topjohnwu.superuser.io.SuFileInputStream
import com.topjohnwu.superuser.io.SuFileOutputStream
import java.io.IOException

object Root {

    val rootAccess: Boolean
        get() = Shell.rootAccess()

    fun writeToFile(text: String, file: String): Boolean {
        val command = "echo \"$text\" > $file"

        return Shell.su(command).exec().isSuccess
    }

    fun writeToMultipleFiles(text: List<String>, files: List<String>): Boolean {
        if (text.size != files.size)
            return Shell.rootAccess()

        var command = ""

        for (i in text.indices) {
            command += "echo \"${text[i]}\" > ${files[i]} && "
        }

        command = command.substring(0, command.lastIndexOf(" && "))

        val returnValue = Shell.su(command).exec().isSuccess

        return returnValue
    }

    private fun writeSingleFile(inputFileAbsolutePath: String, outputFileAbsolutePath: String): Boolean {
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

    fun writeMultipleFiles(inputFilesAbsolutePath: List<String>, outputFilesAbsolutePath: List<String>): Boolean {
        var success = false

        for (i in inputFilesAbsolutePath.indices) {
            success = writeSingleFile(inputFilesAbsolutePath[i], outputFilesAbsolutePath[i])
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