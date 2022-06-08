package com.comphy.photo.ui.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.created.CreatedCommunityResponseContent
import com.comphy.photo.databinding.ItemCommunityBinding
import com.comphy.photo.databinding.ItemOnboardPagerBinding
import com.comphy.photo.databinding.ItemPaymentBinding
import com.comphy.photo.ui.onboard.OnboardPagerAdapter
import com.comphy.photo.ui.onboard.OnboardViewModel
import com.comphy.photo.ui.post.CreatePostAdapter
import splitties.resources.color

class PaymentAdapter (
    private val layoutInflater: LayoutInflater: List<Int>
) : RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: , position: Int) {
        val layoutInflater = listImages[position]

    override fun getItemCount(): Int = 3

    inner class ViewHolder(var binding: ItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root)
}

