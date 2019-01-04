package xyz.z3ro.colorenhance.utility

import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile

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
            command += "echo\"${text[i]}\" > ${files[i]} &&"
        }

        command = command.substring(0, command.lastIndexOf(" &&"))
        Shell.su(command)

        return Shell.su(command).exec().isSuccess
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