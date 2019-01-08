package xyz.z3ro.colorenhance.utility

import xyz.z3ro.colorenhance.model.KCAL
import java.io.*

object FileHelper {

    fun copy(absoluteDestinationPath: String, content: String): Boolean {
        if (createFile(absoluteDestinationPath)) {
            return copyContentBackup(absoluteDestinationPath, content)
        }
        return false
    }

    private fun createFile(absoluteDestinationPath: String): Boolean {
        val file = File(absoluteDestinationPath)
        if (!file.exists()) return file.createNewFile()
        return false
    }

    private fun copyContentBackup(absoluteDestinationPath: String, content: String): Boolean {
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

    fun filesToRestore(absoluteSourcePath: String): List<String> {
        val content = mutableListOf<String>()

        File(absoluteSourcePath).list().forEach { content.add(File(absoluteSourcePath, it).absolutePath) }
        return content
    }

    fun createDirectory(directory: File): Boolean {
        return directory.mkdirs()
    }

    fun list(path: String): ArrayList<String> {
        val directoryList = ArrayList<String>()
        val backupPath = File(path)

        if (backupPath.list().isNotEmpty()) {
            backupPath.list().forEach { directoryList.add(it) }
        }

        return directoryList
    }

    fun kcalToList(kcal: KCAL): List<String> {
        return listOf(
            kcal.switch,
            kcal.rgb,
            kcal.rgbMultiplier,
            kcal.saturationIntensity,
            kcal.hue,
            kcal.screenValue,
            kcal.contrast
        )
    }
}