package xyz.z3ro.colorenhance.utility

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import dmax.dialog.SpotsDialog
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.utility.kcal.KCALManager
import java.io.File
import java.lang.ref.WeakReference

object Operations {

    // File paths
    private const val KCAL_SWITCH = "/sys/devices/platform/kcal_ctrl.0/kcal_enable"
    private const val KCAL_COLOR = "/sys/devices/platform/kcal_ctrl.0/kcal"
    private const val MIN_RGB_MULIPLIER = "/sys/devices/platform/kcal_ctrl.0/kcal_min"
    private const val SAT_INTENSITY = "/sys/devices/platform/kcal_ctrl.0/kcal_sat"
    private const val HUE = "/sys/devices/platform/kcal_ctrl.0/kcal_hue"
    private const val SCREEN_VAL = "/sys/devices/platform/kcal_ctrl.0/kcal_val"
    private const val CONTRAST = "/sys/devices/platform/kcal_ctrl.0/kcal_cont"

    private val paths: List<String> = listOf(
        KCAL_SWITCH, KCAL_COLOR, MIN_RGB_MULIPLIER, SAT_INTENSITY, HUE, SCREEN_VAL,
        CONTRAST
    )

//    private fun backupOperation(destinationDirectory: String): Boolean {
//        if (!KCALManager.kcalEnabled) return false
//        val now: Calendar = Calendar.getInstance()
//        val backupDirectory = File(destinationDirectory, now.time.toString())
//        if (FileHelper.createDirectory(backupDirectory)) {
//            for (path in paths) {
//                val readings = Root.readOneLine(path)
//                val fileName = File(path).name
//                val destinationFilePath = File(backupDirectory, fileName).absolutePath
//                FileHelper.copy(destinationFilePath, readings)
//            }
//        }
//        return false
//    }

    fun backup(context: Context, backupsDirectory: String, backupDirectoryName: String) {
        BackupTask(context).execute(backupsDirectory, backupDirectoryName)
    }

    private class BackupTask(context: Context) : AsyncTask<String, Void, Boolean>() {

        private val contextWeakReference: WeakReference<Context> = WeakReference(context)

        private val progressDialog: AlertDialog = SpotsDialog.Builder().setContext(contextWeakReference.get())
            .setCancelable(false).setTheme(R.style.ProgressDialog).build()

        override fun onPreExecute() {
            progressDialog.show()
        }

        override fun doInBackground(vararg backupDirectory: String?): Boolean {
            if (!KCALManager.kcalEnabled) return false
            var returnValue = false
            val backupDirectoryName = File(backupDirectory[0], backupDirectory[1])
            if (FileHelper.createDirectory(backupDirectoryName)) {
                for (path in paths) {
                    val readings = Root.readOneLine(path)
                    val fileName = File(path).name
                    val destinationFilePath = File(backupDirectoryName, fileName).absolutePath
                    returnValue = FileHelper.copy(destinationFilePath, readings)
                    if (!returnValue) break
                }
            }
            return returnValue
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                progressDialog.dismiss()
                Toast.makeText(contextWeakReference.get(), R.string.backup_successful, Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.dismiss()
                Toast.makeText(contextWeakReference.get(), R.string.failed_to_backup, Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun restore(context: Context, sourceDirectory: String) {
        RestoreTask(context).execute(sourceDirectory)
    }

    private class RestoreTask(context: Context) : AsyncTask<String, Void, Boolean>() {

        private val contextWeakReference: WeakReference<Context> = WeakReference(context)

        private val progressDialog: AlertDialog = SpotsDialog.Builder().setContext(contextWeakReference.get())
            .setCancelable(false).setTheme(R.style.ProgressDialog).build()

        override fun onPreExecute() {
            progressDialog.show()
        }

        override fun doInBackground(vararg sourceDirectory: String?): Boolean {
            return Root.writeMultipleFiles(FileHelper.filesToRestore(sourceDirectory[0]!!), paths)
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                progressDialog.dismiss()
                Toast.makeText(contextWeakReference.get(), R.string.restore_successful, Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.dismiss()
                Toast.makeText(contextWeakReference.get(), R.string.restore_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }
}