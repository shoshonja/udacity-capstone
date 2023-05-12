package com.bikes.greyp.udacitycapstoneproject.ui.parkmap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikes.greyp.udacitycapstoneproject.data.models.RidingSpot
import com.bikes.greyp.udacitycapstoneproject.databinding.SpotRecyclerViewItemBinding

class ParkMapAdapter(
    private val ridingSpotList: List<RidingSpot>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<ParkMapAdapter.RidingSpotViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RidingSpotViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)

        val binding = SpotRecyclerViewItemBinding.inflate(layoutInflater, parent, false)
        return RidingSpotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParkMapAdapter.RidingSpotViewHolder, position: Int) {
        holder.textTitle.text = ridingSpotList[position].title
    }

    override fun getItemCount(): Int {
        return ridingSpotList.size
    }

    inner class RidingSpotViewHolder(binding: SpotRecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        val textTitle = binding.spotRecyclerTitle

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position)
            }
        }

    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }
}