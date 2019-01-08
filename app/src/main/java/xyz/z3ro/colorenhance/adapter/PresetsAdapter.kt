package xyz.z3ro.colorenhance.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_preset.view.*
import xyz.z3ro.colorenhance.R
import xyz.z3ro.colorenhance.interfaces.KcalItemClickListener
import xyz.z3ro.colorenhance.model.KCAL

class PresetsAdapter(private val presets: ArrayList<KCAL>, private val kcalItemClickListener: KcalItemClickListener) :
    RecyclerView.Adapter<PresetsAdapter.PresetsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresetsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_preset, parent, false)
        return PresetsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PresetsViewHolder, position: Int) {
        val preset = presets[position]
        holder.presetName.text = preset.name
        holder.bind(preset, kcalItemClickListener)
    }

    override fun getItemCount(): Int {
        return presets.size
    }

    class PresetsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val presetName: TextView = view.textView_presetName

        fun bind(kcal: KCAL, kcalItemClickListener: KcalItemClickListener) {
            itemView.setOnClickListener { kcalItemClickListener.onClick(kcal) }
        }
    }
}