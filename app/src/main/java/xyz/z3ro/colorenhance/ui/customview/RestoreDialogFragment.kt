package xyz.z3ro.colorenhance.ui.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import kotlinx.android.synthetic.main.dialog_restore.view.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.utility.Constants
import xyz.z3ro.colorenhance.utility.Operations
import java.io.File

class RestoreDialogFragment : AppCompatDialogFragment() {

    companion object {
        const val TAG = "RestoreDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(AppCompatDialogFragment.STYLE_NO_FRAME, R.style.Theme_Dialog_Backup)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_restore, container, false)

        val backupName = arguments!!.getString(Constants.RESTORE_DIALOG, "")
        rootView.textView_restoreDialog.text = rootView.context.getString(R.string.restore_dialog_detail, backupName)
        rootView.button_restoreDialog_ok.setOnClickListener {
            Operations.restore(rootView.context, getRestorePath(backupName))
            dismiss()
        }
        rootView.button_restoreDialog_cancel.setOnClickListener { dismiss() }
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

    private fun getRestorePath(backupFolder: String): String =
        File(activity!!.getExternalFilesDir("backups")!!.toString(), backupFolder).absolutePath


}