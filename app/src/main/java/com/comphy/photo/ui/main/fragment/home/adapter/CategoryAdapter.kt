package com.comphy.photo.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.data.model.CommunityCategoryModel
import com.comphy.photo.databinding.ItemCommunityCategoryBinding

class CategoryAdapter(
    private val categories: List<CommunityCategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.ViewHolder {
        val view =
            ItemCommunityCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val category = categories[position]

        holder.binding.txtCategory.text = category.categoryTitle
        Glide.with(holder.itemView)
            .load(category.categoryIcon)
            .fitCenter()
            .into(holder.binding.imgCategory)
    }

    override fun getItemCount(): Int = categories.size

    inner class ViewHolder(var binding: ItemCommunityCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

}