package xyz.z3ro.colorenhance.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_restore.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.adapter.RestoreAdapter
import xyz.z3ro.colorenhance.interfaces.RestoreItemClickListener
import xyz.z3ro.colorenhance.ui.customview.RestoreDialogFragment
import xyz.z3ro.colorenhance.utility.Constants
import xyz.z3ro.colorenhance.utility.FileHelper
import java.util.*


class RestoreActivity : AppCompatActivity() {

    companion object {
        const val TAG = "RestoreActivity"
    }

    private var restoreList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore)

        setupToolbar()
        getRestoreList()
        setRecyclerView()

    }

    private fun setupToolbar() {
        toolbar_restore.setNavigationOnClickListener { finish() }
    }

    private fun getRestoreList() {
        restoreList = FileHelper.list(getExternalFilesDir("backups")!!.toString())

        if (restoreList.isNotEmpty()) textVIew_nothingToShow.visibility = View.GONE
    }

    private fun setRecyclerView() {
        if (restoreList.isNotEmpty()) {
            val restoreAdapter = RestoreAdapter(
                restoreList,
                RestoreItemClickListener { restoreDialog(it) })
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
            val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
            recyclerView_restore.layoutManager = layoutManager
            recyclerView_restore.itemAnimator = DefaultItemAnimator()
            recyclerView_restore.addItemDecoration(dividerItemDecoration)
            recyclerView_restore.adapter = restoreAdapter
            recyclerView_restore.setItemViewCacheSize(2)

            recyclerView_restore.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.canScrollVertically(Constants.SCROLL_DIRECTION_UP))
                        appBar_restore.elevation = 8.0F
                    else
                        appBar_restore.elevation = 0.0F
                }
            })
        }
    }

    private fun restoreDialog(backupName: String) {
        val restoreDialogFragment = RestoreDialogFragment()
        val arguments = Bundle()
        arguments.putString(Constants.RESTORE_DIALOG, backupName)
        restoreDialogFragment.arguments = arguments
        restoreDialogFragment.show(supportFragmentManager.beginTransaction(), RestoreDialogFragment.TAG)
    }
}