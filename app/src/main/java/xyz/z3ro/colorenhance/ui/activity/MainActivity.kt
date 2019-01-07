package xyz.z3ro.colorenhance.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import kotlinx.android.synthetic.main.activity_main.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.ui.customview.BackupDialogFragment
import xyz.z3ro.colorenhance.utility.Constants
import xyz.z3ro.colorenhance.utility.PreferenceHelper
import xyz.z3ro.colorenhance.utility.Root
import xyz.z3ro.colorenhance.utility.kcal.KCALManager

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardView_preset.setOnClickListener(this)
        cardView_backup.setOnClickListener(this)
        cardView_restore.setOnClickListener(this)
        floatingActionButton_apply.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        CompatibilityChecker().execute()
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.cardView_preset -> {
                    val presetIntent = Intent(this, PresetActivity::class.java)
                    startActivityForResult(presetIntent, Constants.PRESET_ACTIVITY_REQ_CODE)
                }

                R.id.cardView_backup -> {
                    val backupDialogFragment = BackupDialogFragment()
                    backupDialogFragment.show(supportFragmentManager.beginTransaction(), BackupDialogFragment.TAG)
                }

                R.id.cardView_restore -> {
                    val restoreIntent = Intent(this, RestoreActivity::class.java)
                    startActivity(restoreIntent)
                }

                R.id.floatingActionButton_apply -> {

                }
            }
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
                showAlertDialog(
                    R.string.dialog_title_no_root,
                    R.string.dialog_description_no_root
                )
            } else if (!kcalSupported) {
                showAlertDialog(
                    R.string.dialog_title_no_kcal,
                    R.string.dialog_description_no_kcal
                )
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
