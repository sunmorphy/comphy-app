package com.comphy.photo.ui.main.fragment.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.ItemPostPlaceholderBinding

class FeedLoadingStateAdapter(
    private val adapter: FeedAdapter
) : LoadStateAdapter<FeedLoadingStateAdapter.ViewHolder>() {
    override fun onBindViewHolder(
        holder: ViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ViewHolder {
        val view =
            ItemPostPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view) { adapter.retry() }
    }

    class ViewHolder(
        private val binding: ItemPostPlaceholderBinding,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener { retryCallback() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                shimmerPost.isVisible = (loadState == LoadState.Loading)

                if (loadState == LoadState.Loading) shimmerPost.startShimmer()
                else shimmerPost.stopShimmer()

                btnRetry.isVisible = (loadState is LoadState.Error)

                txtErrorMessage.isVisible =
                    !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
                txtErrorMessage.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}