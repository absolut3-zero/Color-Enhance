package xyz.z3ro.colorenhance.customview

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import kotlinx.android.synthetic.main.dialog_backup.*
import xyz.z3ro.colorenhance.R


class BackupDialog(activity: Activity) : Dialog(activity), View.OnClickListener {
    var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_backup)
        button_backupDialog_ok.setOnClickListener(this)
        button_backupDialog_cancel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.button_backupDialog_ok -> backup()
                R.id.button_backupDialog_cancel -> dismiss()
                else -> dismiss()
            }
        }
    }

    fun backup() {
//        Toast.makeText(activity, "LOL", Toast.LENGTH_SHORT).show()
    }
}