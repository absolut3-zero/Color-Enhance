package xyz.z3ro.colorenhance.ui.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_preset.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.adapter.PresetsAdapter
import xyz.z3ro.colorenhance.interfaces.KcalItemClickListener
import xyz.z3ro.colorenhance.model.KCAL
import xyz.z3ro.colorenhance.utility.Constants
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class PresetActivity : AppCompatActivity() {

    private val TAG = "PresetActivity"

    private lateinit var progressDialog: AlertDialog

    private val presets = ArrayList<KCAL>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preset)

        progressDialog = SpotsDialog.Builder().setContext(this)
            .setCancelable(false).setTheme(R.style.ProgressDialog).build()

        setupToolbar()

        progressDialog.show()

        InternetCheckTask().execute()
    }

    private fun setupToolbar() {
        toolbar_presets.setNavigationOnClickListener { finish() }
    }

    inner class InternetCheckTask : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg p0: Void?): Boolean {

            val socket = Socket()
            try {
                socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
                socket.close()
            } catch (e: IOException) {
                return false
            } finally {
                socket.close()
            }

            return true

        }

        override fun onPostExecute(result: Boolean?) {
            if (result != null) internetAccessResult(result)
        }
    }

    private fun internetAccessResult(result: Boolean) {
        if (result) {
            if (imageView_noConnection.visibility == View.VISIBLE) imageView_noConnection.visibility = View.GONE
            if (textView_noConnection.visibility == View.VISIBLE) textView_noConnection.visibility = View.GONE
            populateList()
        } else {
            recyclerView_presets.visibility = View.GONE
            progressDialog.dismiss()
        }
    }

    private fun populateList() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("presets")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    presets.add(
                        KCAL(
                            name = it.key!!,
                            switch = it.child("switch").value.toString(),
                            rgb = it.child("rgb").value.toString(),
                            rgbMultiplier = it.child("rgbMultiplier").value.toString(),
                            saturationIntensity = it.child("saturationIntensity").value.toString(),
                            hue = it.child("hue").value.toString(),
                            screenValue = it.child("screenValue").value.toString(),
                            contrast = it.child("contrast").value.toString()
                        )
                    )
                }

                setRecyclerView()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, databaseError.toString())
            }
        })
    }

    private fun setRecyclerView() {
        if (presets.isNotEmpty()) {
            val presetsAdapter = PresetsAdapter(
                presets,
                KcalItemClickListener { result(it) })
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
            val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)

            recyclerView_presets.layoutManager = layoutManager
            recyclerView_presets.itemAnimator = DefaultItemAnimator()
            recyclerView_presets.addItemDecoration(dividerItemDecoration)
            recyclerView_presets.adapter = presetsAdapter
            recyclerView_presets.setItemViewCacheSize(2)

            recyclerView_presets.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.canScrollVertically(Constants.SCROLL_DIRECTION_UP))
                        appBar_presets.elevation = 8.0F
                    else
                        appBar_presets.elevation = 0.0F
                }
            })
        }
        progressDialog.dismiss()
    }

    private fun result(kcal: KCAL) {
        val returnIntent = Intent().putExtra(Constants.PRESET_INTENT_EXTRA_CODE, kcal)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}