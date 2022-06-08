package com.comphy.photo.ui.main.fragment.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.event.EventResponseContentItem
import com.comphy.photo.databinding.ItemTipsBinding
import splitties.resources.drawable

class TipsAdapter(
    private val articles: List<EventResponseContentItem>,
    private val onClick: (
        article: EventResponseContentItem,
        nextArticle: EventResponseContentItem?
    ) -> Unit
) : RecyclerView.Adapter<TipsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TipsAdapter.ViewHolder {
        val view = ItemTipsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipsAdapter.ViewHolder, position: Int) {
        val article = articles[position]
        val nextArticle: EventResponseContentItem? =
            if ((articles.indexOf(article) - 1) == articles.size) articles[articles.indexOf(article) + 1]
            else null

        with(holder.binding) {
            txtTipsTitle.text = article.eventName
            Glide.with(holder.itemView)
                .load(article.linkBanner)
                .placeholder(holder.itemView.drawable(R.drawable.img_banner_placeholder))
                .error(holder.itemView.drawable(R.drawable.img_banner_placeholder))
                .into(imgTipsThumb)
        }

        holder.itemView.setOnClickListener { onClick(article, nextArticle) }
    }

    override fun getItemCount(): Int = articles.size

    inner class ViewHolder(var binding: ItemTipsBinding) : RecyclerView.ViewHolder(binding.root)
}