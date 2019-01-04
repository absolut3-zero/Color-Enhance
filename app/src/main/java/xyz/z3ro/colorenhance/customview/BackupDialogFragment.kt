package xyz.z3ro.colorenhance.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_backup.view.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.utility.Operations
import java.util.*

class BackupDialogFragment : AppCompatDialogFragment() {

    companion object {
        val TAG = "BackupDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Dialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_backup, container, false)
        rootView.editText_backupDialog.setText(defaultName())

        rootView.button_backupDialog_ok.setOnClickListener {
            Operations.backup(
                rootView.context,
                activity!!.getExternalFilesDir("backups")!!.absolutePath,
                rootView.editText_backupDialog.text.toString()
            )
            dismiss()
        }

        rootView.button_backupDialog_cancel.setOnClickListener { dismiss() }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = 900
            val height = 600
            dialog.window!!.setLayout(width, height)
        }
    }

    private fun defaultName(): String {
        val now: Calendar = Calendar.getInstance()
        return now.time.toString()
    }
}