package xyz.z3ro.colorenhance.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.utility.Constants
import xyz.z3ro.colorenhance.utility.PreferenceHelper

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "SettingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbar_settings.setNavigationOnClickListener { finish() }

        switch_applyOnBoot.isChecked = PreferenceHelper.getBoolean(this, Constants.APPLY_ON_BOOT, false)
        switch_notifications.isChecked = PreferenceHelper.getBoolean(this, Constants.NOTIFICATIONS, false)

        switch_applyOnBoot.setOnClickListener(this)
        switch_notifications.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.switch_applyOnBoot -> {
                    if (PreferenceHelper.getBoolean(this, Constants.APPLY_ON_BOOT, false)) {
                        PreferenceHelper.putBoolean(this, Constants.APPLY_ON_BOOT, false)
                    } else PreferenceHelper.putBoolean(this, Constants.APPLY_ON_BOOT, true)
                }

                R.id.switch_notifications -> {
                    if (PreferenceHelper.getBoolean(this, Constants.NOTIFICATIONS, false)) {
                        PreferenceHelper.putBoolean(this, Constants.NOTIFICATIONS, false)
                    } else PreferenceHelper.putBoolean(this, Constants.NOTIFICATIONS, true)
                }
            }
        } else throw NullPointerException()
    }
}