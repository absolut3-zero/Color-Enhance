package xyz.z3ro.colorenhance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_restore.view.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.interfaces.RestoreItemClickListener

class RestoreAdapter(
    private val backups: ArrayList<String>,
    private val restoreItemClickListener: RestoreItemClickListener
) : RecyclerView.Adapter<RestoreAdapter.RestoreViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoreViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_restore, parent, false)
        return RestoreViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RestoreViewHolder, position: Int) {
        val backupName = backups[position]
        holder.backupName.text = backupName
        holder.bind(backupName, restoreItemClickListener)
    }

    override fun getItemCount(): Int {
        return backups.size
    }

    class RestoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val backupName: TextView = view.textView_restoreName

        fun bind(path: String, restoreItemClickListener: RestoreItemClickListener) {
            itemView.setOnClickListener { restoreItemClickListener.onClick(path) }
        }
    }
}