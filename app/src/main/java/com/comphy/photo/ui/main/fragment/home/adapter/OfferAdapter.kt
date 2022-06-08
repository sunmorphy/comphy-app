package com.comphy.photo.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.data.model.OfferModel
import com.comphy.photo.databinding.ItemOfferBinding

class OfferAdapter(
    private val offers: List<OfferModel>,
    private val onClick: (url: String) -> Unit
) : RecyclerView.Adapter<OfferAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferAdapter.ViewHolder {
        val view = ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferAdapter.ViewHolder, position: Int) {
        val offer = offers[position]

        with(holder.binding) {
            Glide.with(holder.itemView)
                .load(offer.image)
                .fitCenter()
                .into(imgOfferLogo)
        }
        holder.itemView.setOnClickListener { onClick(offer.url) }
    }

    override fun getItemCount(): Int = offers.size

    inner class ViewHolder(var binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root)
}