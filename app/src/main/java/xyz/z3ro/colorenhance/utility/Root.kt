package xyz.z3ro.colorenhance.utility

import eu.chainfire.libsuperuser.Shell

object Root {

    val rootAccess: Boolean
        get() = Shell.SU.available()

    fun writeToFile(text: String, file: String): Boolean {
        val command = "echo \"$text\" > $file"
        Shell.SU.run(command)

        return rootAccess
    }

    fun writeToMultipleFiles(text: List<String>, files: List<String>): Boolean {
        if (text.size != files.size)
            return Shell.SU.available()

        var command = ""

        for (i in text.indices) {
            command += "echo\"${text[i]}\" > ${files[i]} &&"
        }
        return rootAccess
    }

    fun readContents(file: String): List<String>? {
        return Shell.SU.run("cat $file")
    }

    fun readOneLine(file: String): String {
        val output = readContents(file)
        return if (output == null || output.isEmpty()) "" else output[0]
    }

    fun doesFileExist(file: String): Boolean {
        val output = Shell.SU.run("ls $file")
        return output != null && !output.isEmpty() && output[0] == file
    }
}