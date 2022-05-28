package com.comphy.photo.ui.main.fragment.feed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.databinding.FragmentFeedMainBinding
import com.comphy.photo.ui.main.fragment.feed.FeedAdapter
import com.comphy.photo.ui.main.fragment.feed.FeedViewModel
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.launch
import splitties.toast.toast

class FeedMainFragment : Fragment() {

    private var _binding: FragmentFeedMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by activityViewModels()
    private var player: ExoPlayer? = null
    private var feedAdapter: FeedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getFeedPost()
        }

        setupRecyclerView()
        viewModel.feedResponse.observe(viewLifecycleOwner) {
            lifecycleScope.launch { feedAdapter?.submitData(it) }
        }
        viewModel.errorNorException.observe(viewLifecycleOwner) { toast(it) }
    }

    override fun onPause() {
        player?.stop()
        super.onPause()
    }

    override fun onStop() {
        player?.release()
        super.onStop()
    }

    private fun setupRecyclerView() {
        val feedLayoutManager = LinearLayoutManager(activity)
        feedAdapter = FeedAdapter(player) { pos, postId, isLiked ->
            lifecycleScope.launch {
                if (isLiked) viewModel.unlikePost(postId) else viewModel.likePost(postId)
                viewModel.successResponse.observe(viewLifecycleOwner) { feedAdapter!!.notifyItemChanged(pos) }
            }
        }
        binding.rvFeed.apply {
            layoutManager = feedLayoutManager
            adapter = feedAdapter
        }
        binding.rvFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                player?.stop()
            }
        })
//        viewModel.successResponse.observe(viewLifecycleOwner) {
//            feedAdapter!!.refresh()
//            lifecycleScope.launch { viewModel.getFeedPost() }
//            feedAdapter.notifyDataSetChanged()
//            feedAdapter!!.notifyItemRangeChanged(
//                (binding.rvFeed.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition(),
//                (binding.rvFeed.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//            )
//            feedAdapter!!.notifyItemChanged(feedLayoutManager.findFirstVisibleItemPosition(), feedAdapter.)
//        }
    }
}