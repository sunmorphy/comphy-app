package com.comphy.photo.ui.event.all

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.ItemTrainingAllBinding
import splitties.resources.drawable

class AllEventAdapter(
    private val onClick: (event: EventResponseContentItem) -> Unit
) : RecyclerView.Adapter<AllEventAdapter.ViewHolder>() {

    val events = mutableListOf<EventResponseContentItem>()

    fun setData(events: List<EventResponseContentItem>) {
        this.events.clear()
        this.events.addAll(events)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllEventAdapter.ViewHolder {
        val view = ItemTrainingAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllEventAdapter.ViewHolder, position: Int) {
        val event = events[position]

        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(event.linkBanner)
                .placeholder(holder.itemView.drawable(R.drawable.img_banner_placeholder))
                .error(holder.itemView.drawable(R.drawable.img_banner_placeholder))
                .centerCrop()
                .into(imgTraining)

            txtEventTitle.text = event.eventName
            txtEventDescription.text = event.description
            txtEventDate.text // TODO: PARSE TIMESTAMP
        }
        holder.itemView.setOnClickListener { onClick(event) }
    }

    override fun getItemCount(): Int = events.size

    inner class ViewHolder(var binding: ItemTrainingAllBinding) : RecyclerView.ViewHolder(binding.root)
}