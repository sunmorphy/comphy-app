package com.comphy.photo.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.databinding.ItemTipsBinding

class TipsAdapter : RecyclerView.Adapter<TipsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TipsAdapter.ViewHolder {
        val view = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipsAdapter.ViewHolder, position: Int) {
        holder.binding.txtTipsTitle.text =
            "4 Trik Fotografi Sederhana yang Bisa Kamu Lakukan di Rumah."
        Glide.with(holder.itemView)
            .load(splitties.material.colors.R.color.amber_50)
            .into(holder.binding.imgTipsThumb)
    }

    override fun getItemCount(): Int = 4

    inner class ViewHolder(var binding: ItemTipsBinding) : RecyclerView.ViewHolder(binding.root)
}