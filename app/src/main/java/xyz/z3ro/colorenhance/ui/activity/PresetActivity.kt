package xyz.z3ro.colorenhance.ui.activity

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_preset.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.model.KCAL
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

class PresetActivity : AppCompatActivity() {

    val TAG = this.localClassName

    private lateinit var progressDialog: AlertDialog

    private val presets = mutableListOf<KCAL>()

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
            Log.d(this.localClassName, "Connected")
            if (imageView_noConnection.visibility == View.VISIBLE) imageView_noConnection.visibility = View.GONE
            if (textView_noConnection.visibility == View.VISIBLE) textView_noConnection.visibility = View.GONE
            populateList()
        } else {
            Log.d(this.localClassName, "Not Connected")
            recyclerView_presets.visibility = View.GONE
            progressDialog.dismiss()
        }
    }

    private fun populateList() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("presets")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { presets.add(it.getValue(KCAL::class.java)!!) }
                presets.forEach { Log.d(TAG, it.toString()) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, databaseError.toString())
            }
        })
    }
}