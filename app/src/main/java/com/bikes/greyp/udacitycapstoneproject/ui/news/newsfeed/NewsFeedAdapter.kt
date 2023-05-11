package com.bikes.greyp.udacitycapstoneproject.ui.news.newsfeed

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikes.greyp.udacitycapstoneproject.data.models.PartialFeedItem
import com.bikes.greyp.udacitycapstoneproject.data.models.RssSource
import com.bikes.greyp.udacitycapstoneproject.data.network.RssImageLoader
import com.bikes.greyp.udacitycapstoneproject.databinding.RecyclerViewItemBinding

class NewsFeedAdapter(
    private val partialFeedItemList: List<PartialFeedItem>,
    private val rssSource: RssSource,
    private val clickListener: NewsFeedAdapter.ClickListener
) : RecyclerView.Adapter<NewsFeedAdapter.RssViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)

        val binding = RecyclerViewItemBinding.inflate(layoutInflater, parent, false)
        return RssViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
        holder.textTitle.text = partialFeedItemList[position].title
        holder.textDescription.text = partialFeedItemList[position].description

        when (rssSource) {
            RssSource.Vital -> RssImageLoader.loadVtWIthGlide(
                context,
                partialFeedItemList[position].imageUrl,
                holder.textImage
            )
            RssSource.Pinkbike -> RssImageLoader.loadPbWIthGlide(
                context,
                partialFeedItemList[position].imageUrl,
                holder.textImage
            )
        }
    }

    override fun getItemCount(): Int {
        return partialFeedItemList.size
    }

    inner class RssViewHolder(private val binding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val textTitle = binding.recyclerItemTitle
        val textDescription = binding.recyclerItemDescription
        val textImage = binding.recyclerItemImage

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