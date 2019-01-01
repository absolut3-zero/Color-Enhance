package xyz.z3ro.colorenhance.utilities

import java.io.*

object FileHelper {

    fun copy(absoluteDestinationPath: String, content: String): Boolean {
        if (createFile(absoluteDestinationPath)) {
            return copyContent(absoluteDestinationPath, content)
        }
        return false
    }

    private fun createFile(absoluteDestinationPath: String): Boolean {
        val file = File(absoluteDestinationPath)
        if (!file.exists()) return file.createNewFile()
        return false
    }

    private fun copyContent(absoluteDestinationPath: String, content: String): Boolean {
        val inputStream: InputStream = ByteArrayInputStream(content.toByteArray(Charsets.UTF_8))
        val outputStream: OutputStream = FileOutputStream(absoluteDestinationPath)
        var returnValue = false

        try {
            inputStream.copyTo(outputStream, 65536)
            returnValue = true
        } catch (e: IOException) {
            e.printStackTrace()
            returnValue = false
        } finally {
            inputStream.close()
            outputStream.close()
        }
        return returnValue
    }

    fun createDirectory(directory: File): Boolean {
        return directory.mkdirs()
    }
}