package com.sriram.vidyarthibus.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sriram.vidyarthibus.R
import com.sriram.vidyarthibus.model.Alternative

class AlternativeAdapter(private var alternatives: List<Alternative>) : 
    RecyclerView.Adapter<AlternativeAdapter.AlternativeViewHolder>() {

    class AlternativeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAltName: TextView = view.findViewById(R.id.tvAltName)
        val tvAltArea: TextView = view.findViewById(R.id.tvAltArea)
        val tvAltPhone: TextView = view.findViewById(R.id.tvAltPhone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlternativeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alternative, parent, false)
        return AlternativeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlternativeViewHolder, position: Int) {
        val alt = alternatives[position]
        holder.tvAltName.text = alt.name
        holder.tvAltArea.text = alt.area
        holder.tvAltPhone.text = "📞 ${alt.phone}"

        holder.tvAltPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${alt.phone}"))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = alternatives.size

    fun updateData(newAlternatives: List<Alternative>) {
        this.alternatives = newAlternatives
        notifyDataSetChanged()
    }
}