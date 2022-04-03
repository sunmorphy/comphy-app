package com.comphy.photo.ui.onboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.databinding.ItemPagerBinding

class OnboardPagerAdapter(private val listImages: List<Int>) :
    RecyclerView.Adapter<OnboardPagerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardPagerAdapter.ViewHolder {
        val view = ItemPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardPagerAdapter.ViewHolder, position: Int) {
        val image = listImages[position]

        Glide.with(holder.itemView)
            .load(image)
            .fitCenter()
            .into(holder.binding.imgOnboardSlide)
    }

    override fun getItemCount(): Int = 3

    inner class ViewHolder(var binding: ItemPagerBinding) : RecyclerView.ViewHolder(binding.root)
}