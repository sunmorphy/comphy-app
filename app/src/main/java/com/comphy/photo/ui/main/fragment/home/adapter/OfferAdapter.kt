package com.comphy.photo.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.databinding.ItemOfferBinding

class OfferAdapter : RecyclerView.Adapter<OfferAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OfferAdapter.ViewHolder {
        val view = ItemOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferAdapter.ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(R.drawable.img_shutterstock)
            .fitCenter()
            .into(holder.binding.imgOfferLogo)
    }

    override fun getItemCount(): Int = 9

    inner class ViewHolder(var binding: ItemOfferBinding) : RecyclerView.ViewHolder(binding.root)
}