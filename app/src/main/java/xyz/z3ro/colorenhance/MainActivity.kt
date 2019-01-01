package xyz.z3ro.colorenhance

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import kotlinx.android.synthetic.main.activity_main.*
import xyz.z3ro.colorenhance.utilities.Constants
import xyz.z3ro.colorenhance.utilities.Operations
import xyz.z3ro.colorenhance.utilities.PreferenceHelper
import xyz.z3ro.colorenhance.utilities.Root
import xyz.z3ro.colorenhance.utilities.kcal.KCALManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CompatibilityChecker().execute()
        fabInitialisation()

    }

    private fun fabInitialisation() {
        floatingActionButton_apply.setOnClickListener {
            val destinationDirectory = getExternalFilesDir("backups")
            Operations.backup(destinationDirectory!!.absolutePath, this)
        }
    }


    private inner class CompatibilityChecker : AsyncTask<Void, Void, String>() {
        internal var rootAccessAvailable = false
        internal var kcalSupported = false

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg p0: Void?): String? {
            rootAccessAvailable = Root.rootAccess
            kcalSupported = KCALManager.isKCALAvailable
            return null
        }

        override fun onPostExecute(result: String?) {
            if (!rootAccessAvailable) {
                showAlertDialog(R.string.dialog_title_no_root, R.string.dialog_description_no_root)
            } else if (!kcalSupported) {
                showAlertDialog(R.string.dialog_title_no_kcal, R.string.dialog_description_no_kcal)
            } else {
                PreferenceHelper.putBoolean(this@MainActivity, Constants.COMPATIBILITY_CHECK, true)
            }
        }
    }

    private fun showAlertDialog(caption: Int, msg: Int) {
        if (isFinishing || isDestroyed) return
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AppTheme))
        builder.setTitle(caption)
        builder.setMessage(msg)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> finish() }
        builder.show()
    }
}
