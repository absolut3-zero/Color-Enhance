package xyz.z3ro.colorenhance

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import kotlinx.android.synthetic.main.activity_main.*
import xyz.z3ro.colorenhance.customview.BackupDialogFragment
import xyz.z3ro.colorenhance.fragment.RestoreFragment
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

    fun backup() {

    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.cardView_preset -> {
                    Toast.makeText(this, "lol", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "lmao", Toast.LENGTH_SHORT).show()
                    Log.d("lol", "LOL")
                }

                R.id.cardView_backup -> {
                    val backupDialogFragment = BackupDialogFragment()
                    backupDialogFragment.show(supportFragmentManager.beginTransaction(), BackupDialogFragment.TAG)
                }

                R.id.cardView_restore -> {
                    val restoreFragment = RestoreFragment()
                    val fragmentManager = supportFragmentManager.beginTransaction()
                    fragmentManager.setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                    fragmentManager.add(R.id.main_layout, restoreFragment, RestoreFragment.TAG)
                    fragmentManager.commit()
                }

                R.id.floatingActionButton_apply -> {
//                    Toast.makeText(this, "lol", Toast.LENGTH_SHORT).show()
//                    Snackbar.make(
//                        findViewById(R.id.main_layout),
//                        R.string.restore_failed,
//                        Snackbar.LENGTH_LONG
//                    ).show()

//                    Operations.backup(getExternalFilesDir("backups")!!.absolutePath, this)
                }
            }
        }
    }

//    private fun CompatibilityChecker() {
//        var rootAccess = false
//        var kcalSupport = false
//
//        val rootAccessCheck = Thread(Runnable { rootAccess = Root.rootAccess })
//        rootAccessCheck.priority = Thread.NORM_PRIORITY
//
//        val kcalSupportCheck = Thread(Runnable { kcalSupport = KCALManager.isKCALAvailable })
//        kcalSupportCheck.priority = Thread.NORM_PRIORITY
//
//        rootAccessCheck.start()
//        kcalSupportCheck.start()
//    }

    private inner class CompatibilityChecker : AsyncTask<Void, Void, String>() {
        internal var rootAccessAvailable = false
        internal var kcalSupported = false

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg p0: Void?): String? {
            rootAccessAvailable = Root.rootAccess
//            val kcalSupportThread = Thread(Runnable { kcalSupported = KCALManager.isKCALAvailable })
//            kcalSupportThread.priority = Thread.NORM_PRIORITY
//            kcalSupportThread.start()
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
