package com.comphy.photo.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.databinding.ItemHomePagerBinding

class HomePagerAdapter(
    private val listImages: List<String>
) : RecyclerView.Adapter<HomePagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePagerAdapter.ViewHolder {
        val view = ItemHomePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomePagerAdapter.ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(listImages[position])
            .centerCrop()
            .into(holder.binding.imgHomeSlide)
    }

    override fun getItemCount(): Int = listImages.size

    inner class ViewHolder(var binding: ItemHomePagerBinding) :
        RecyclerView.ViewHolder(binding.root)
}