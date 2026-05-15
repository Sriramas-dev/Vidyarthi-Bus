package com.sriram.vidyarthibus.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sriram.vidyarthibus.R
import com.sriram.vidyarthibus.model.BusRoute

class RouteAdapter(
    private var routes: List<BusRoute>,
    private val onItemClick: (BusRoute) -> Unit
) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    private var selectedPosition = -1

    class RouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRouteId: TextView = view.findViewById(R.id.tvRouteId)
        val tvRouteName: TextView = view.findViewById(R.id.tvRouteName)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val cardView: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_route, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val route = routes[position]
        holder.tvRouteId.text = route.routeId
        holder.tvRouteName.text = route.routeName

        if (selectedPosition == position) {
            holder.cardView.setBackgroundResource(R.drawable.rounded_blue_bg)
            holder.tvRouteId.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.textWhite))
            holder.tvRouteName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.textWhite))
        } else {
            holder.cardView.setBackgroundResource(0)
            holder.cardView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, android.R.color.white))
            holder.tvRouteId.setTextColor(ContextCompat.getColor(holder.itemView.context, android.R.color.black))
            holder.tvRouteName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.textMuted))
        }

        val (bgColorRes, textColorRes, label) = when (route.crowdStatus.uppercase().trim()) {
            "EMPTY" -> Triple(R.color.statusEmptyBgLight, R.color.statusEmptyTextLight, "Empty")
            "FILLING" -> Triple(R.color.statusFillingBgLight, R.color.statusFillingTextLight, "Filling")
            "FULL" -> Triple(R.color.statusFullBgLight, R.color.statusFullTextLight, "Full")
            else -> Triple(R.color.statusNoDataBgLight, R.color.statusNoDataTextLight, "No Data")
        }

        holder.tvStatus.text = label
        holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, textColorRes))
        
        val background = holder.tvStatus.background as? GradientDrawable
        background?.setColor(ContextCompat.getColor(holder.itemView.context, bgColorRes))

        holder.itemView.setOnClickListener {
            val oldPos = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(oldPos)
            notifyItemChanged(selectedPosition)
            onItemClick(route)
        }
    }

    override fun getItemCount() = routes.size

    fun updateData(newRoutes: List<BusRoute>) {
        this.routes = newRoutes
        notifyDataSetChanged()
    }
}