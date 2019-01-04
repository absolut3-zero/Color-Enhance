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
    private val KCAL_SWITCH = "/sys/devices/platform/kcal_ctrl.0/kcal_enable"
    private val KCAL_COLOR = "/sys/devices/platform/kcal_ctrl.0/kcal"
    private val MIN_RGB_MULIPLIER = "/sys/devices/platform/kcal_ctrl.0/kcal_min"
    private val SAT_INTENSITY = "/sys/devices/platform/kcal_ctrl.0/kcal_sat"
    private val HUE = "/sys/devices/platform/kcal_ctrl.0/kcal_hue"
    private val SCREEN_VAL = "/sys/devices/platform/kcal_ctrl.0/kcal_val"
    private val CONTRAST = "/sys/devices/platform/kcal_ctrl.0/kcal_cont"

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

    fun backup(context: Context, backupDirectory: String, destinationDirectory: String) {
        BackupTask(context).execute(backupDirectory, destinationDirectory)
    }

    private class BackupTask(context: Context) : AsyncTask<String, Void, Boolean>() {

        private val contextWeakReference: WeakReference<Context> = WeakReference(context)

        private val progressDialog: AlertDialog = SpotsDialog.Builder().setContext(contextWeakReference.get())
            .setCancelable(false).setTheme(R.style.ProgressDialog).build()

        override fun onPreExecute() {
            progressDialog.show()
        }

        override fun doInBackground(vararg destinationDirectory: String?): Boolean {
            if (!KCALManager.kcalEnabled) return false
            var returnValue = false
            val backupDirectory = File(destinationDirectory[0], destinationDirectory[1])
            if (FileHelper.createDirectory(backupDirectory)) {
                for (path in paths) {
                    val readings = Root.readOneLine(path)
                    val fileName = File(path).name
                    val destinationFilePath = File(backupDirectory, fileName).absolutePath
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


    fun restore(targetDirect: String, context: Context) {
        RestoreTask(context).execute(targetDirect)
    }

    private class RestoreTask(context: Context) : AsyncTask<String, Void, Boolean>() {

        private val contextWeakReference: WeakReference<Context> = WeakReference(context)

        private val progressDialog: AlertDialog = SpotsDialog.Builder().setContext(contextWeakReference.get())
            .setCancelable(false).setTheme(R.style.ProgressDialog).build()

        override fun onPreExecute() {
            progressDialog.show()
        }

        override fun doInBackground(vararg targetDirectory: String?): Boolean {

            return false
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