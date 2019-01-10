package xyz.z3ro.colorenhance.ui.activity

import android.animation.ValueAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.animation.addListener
import androidx.core.view.ViewCompat
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.model.KCAL
import xyz.z3ro.colorenhance.ui.customview.BackupDialogFragment
import xyz.z3ro.colorenhance.utility.Constants
import xyz.z3ro.colorenhance.utility.Operations
import xyz.z3ro.colorenhance.utility.PreferenceHelper
import xyz.z3ro.colorenhance.utility.Root
import xyz.z3ro.colorenhance.utility.kcal.KCALManager
import java.io.File

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var kcal: KCAL? = null

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        collapseBottomAppBar()

        setSupportActionBar(bottomAppBar)

        cardView_preset.setOnClickListener(this)
        cardView_backup.setOnClickListener(this)
        cardView_restore.setOnClickListener(this)
        floatingActionButton_apply.setOnClickListener(this)
        switch_mainSwitch.setOnClickListener(this)

    }

    override fun onStart() {
        super.onStart()
        CompatibilityChecker().execute()

        createNotificationChannel()

        val enabled = PreferenceHelper.getBoolean(this, Constants.MAIN_SWITCH, false)
        setMainSwitch(enabled)
        setEnabled(enabled)
        setFAB(enabled)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bottom_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
            }

            R.id.about -> {

            }
        }
        return super.onOptionsItemSelected(item)
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
                    fabOnClick()
                }

                R.id.switch_mainSwitch -> {
                    if (PreferenceHelper.getBoolean(this, Constants.MAIN_SWITCH, false)) {
                        PreferenceHelper.putBoolean(this, Constants.MAIN_SWITCH, false)
                        // Disable
                        Operations.restore(
                            this,
                            File(getExternalFilesDir("Temp")!!.absolutePath, "Default Backup").absolutePath
                        )
                        setEnabled(false)
                        setFAB(false)
                    } else {
                        if (!PreferenceHelper.getBoolean(this, Constants.ENABLED_STATUS, false)) {
                            Toast.makeText(this, R.string.no_preset_selected, Toast.LENGTH_SHORT).show()
                            setMainSwitch(false)
                        } else {
                            PreferenceHelper.putBoolean(this, Constants.MAIN_SWITCH, true)
                            // Enable
                            Operations.restore(
                                this,
                                File(getExternalFilesDir("Temp")!!.absolutePath, "Current Preset").absolutePath
                            )
                            setEnabled(true)
                            setFAB(true)
                        }
                    }
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            Constants.PRESET_ACTIVITY_REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    kcal = data!!.getParcelableExtra(Constants.PRESET_INTENT_EXTRA_CODE)
                    setBottomAppBar()
                }
            }
        }
    }

    private fun setMainSwitch(enabled: Boolean) {
        switch_mainSwitch.isChecked = enabled
    }

    private fun setBottomAppBar() {
        textView_selectedPreset.text = kcal?.name ?: ""
    }

    private fun fabOnClick() {
        if (kcal == null) {
            Toast.makeText(this, R.string.no_preset_selected, Toast.LENGTH_SHORT).show()
            return
        }
        Operations.presetApply(this, kcal!!)

        PreferenceHelper.putBoolean(this, Constants.ENABLED_STATUS, true)
        PreferenceHelper.putBoolean(this, Constants.MAIN_SWITCH, true)
        PreferenceHelper.putString(this, Constants.PRESET_APPLIED_NAME, kcal?.name ?: "")
        setMainSwitch(true)
        setEnabled(true)
        setFAB(true)

        kcal = null
        setBottomAppBar()
    }

    private fun setFAB(enable: Boolean) {
        if (enable) {
            bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        } else bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
    }

    private fun setEnabled(enable: Boolean) {
        if (enable) {
            textView_enabledStatus.text = getString(R.string.enabled)
            if (PreferenceHelper.getString(this, Constants.PRESET_APPLIED_NAME, "").equals("")) {
                textView_presetApplied.visibility = View.GONE
            } else {
                textView_presetApplied.visibility = View.VISIBLE
                val text = getString(
                    R.string.preset_applied_name,
                    PreferenceHelper.getString(this, Constants.PRESET_APPLIED_NAME, "")
                )
                textView_presetApplied.text = text
            }
        } else {
            textView_enabledStatus.text = getString(R.string.disabled)
            textView_presetApplied.visibility = View.GONE
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

                if (PreferenceHelper.getBoolean(this@MainActivity, Constants.FIRST_RUN, true)) {
                    PreferenceHelper.putString(
                        this@MainActivity,
                        Constants.TEMP_FOLDER,
                        getExternalFilesDir("Temp")!!.absolutePath
                    )
                    Operations.defaultBackup(
                        this@MainActivity,
                        getExternalFilesDir("Temp")!!.absolutePath,
                        "Default Backup"
                    )

                }
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

    private fun createNotificationChannel() {

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            importance
        ).apply {
            description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun collapseBottomAppBar() {
        val typedValue = TypedValue()
        theme.resolveAttribute(android.R.attr.actionBarSize, typedValue, true)
        val appbarHeight = TypedValue.complexToDimension(typedValue.data, resources.displayMetrics)

        val valueAnimator = ValueAnimator.ofInt(180, appbarHeight.toInt())
        valueAnimator.addUpdateListener {
            val layoutParameters: ViewGroup.LayoutParams = bottomAppBar.layoutParams
            layoutParameters.height = it.animatedValue as Int
            bottomAppBar.layoutParams = layoutParameters
        }

        valueAnimator.start()
        valueAnimator.addListener {
            ViewCompat.animate(floatingActionButton_apply).setStartDelay(2000).setDuration(2000).scaleY(1.0F)
                .scaleX(1.0F)
                .start()
        }
    }
}
