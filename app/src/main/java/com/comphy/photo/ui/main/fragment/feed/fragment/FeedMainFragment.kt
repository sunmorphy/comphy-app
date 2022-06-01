package com.comphy.photo.ui.main.fragment.feed.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.FragmentFeedMainBinding
import com.comphy.photo.ui.comment.main.CommentActivity
import com.comphy.photo.ui.main.MainViewModel
import com.comphy.photo.ui.main.fragment.feed.FeedAdapter
import com.comphy.photo.ui.main.fragment.feed.FeedFragment
import com.comphy.photo.ui.main.fragment.feed.FeedLoadingStateAdapter
import com.comphy.photo.ui.main.fragment.feed.FeedViewModel
import com.comphy.photo.ui.main.fragment.feed.viewholder.FeedVideoViewHolder
import com.comphy.photo.vo.FollowType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.toast.toast

class FeedMainFragment : Fragment() {

    companion object {
        private const val EXTRA_POST_ID = "extra_post_id"
        private const val EXTRA_COMMENT_COUNT = "extra_comment_count"
        private const val EXTRA_USER_DATA = "extra_user_data"
    }

    private var _binding: FragmentFeedMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FeedViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private var feedAdapter: FeedAdapter? = null
    private var videoViewHolder: FeedVideoViewHolder? = null

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

        setupObserver()
    }

    private fun setupRecyclerView(userData: UserResponseData) {
        feedAdapter = FeedAdapter(
            userData = userData,
            onProfileClick = { toast("USER DENGAN ID : $it") }, // TODO: API NOT READY YET
            onFollowClick = { userId, isFollowed ->
                lifecycleScope.launch {
                    when (isFollowed) {
                        FollowType.NOT_FOLLOWED -> viewModel.followUser(userId)
                        FollowType.FOLLOWED -> viewModel.unfollowUser(userId)
                    }
                }
            },
            onBookmarkClick = { pos, postId, isSaved ->
                lifecycleScope.launch {
                    if (isSaved) viewModel.unbookmarkUser(postId, pos)
                    else viewModel.bookmarkUser(postId, pos)
                }
            },
            onLikeClick = { pos, postId, isLiked ->
                lifecycleScope.launch {
                    if (isLiked) viewModel.unlikePost(postId, pos)
                    else viewModel.likePost(postId, pos)
                }
            },
            onCommentClick = { postId, commentCount ->
                start<CommentActivity> {
                    putExtra(EXTRA_POST_ID, postId)
                    putExtra(EXTRA_COMMENT_COUNT, commentCount)
                    putExtra(EXTRA_USER_DATA, userData)
                }
            },
            videoHolder = { holder ->
                (parentFragment as FeedFragment).binding.mainView.setOnScrollChangeListener(
                    NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
                        holder.pausePlayer()
                    }
                )
            }
        )

        binding.rvFeed.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = feedAdapter
            setHasFixedSize(false)
        }

        lifecycleScope.launch {
            viewModel.getFeedPost().collectLatest { feedAdapter!!.submitData(it) }
        }

        feedAdapter!!.withLoadStateHeaderAndFooter(
            FeedLoadingStateAdapter(feedAdapter!!),
            FeedLoadingStateAdapter(feedAdapter!!)
        )
    }

    private fun setupObserver() {
        mainViewModel.userData.observe(viewLifecycleOwner) { setupRecyclerView(it) }
        viewModel.successResponse.observe(viewLifecycleOwner) { feedAdapter!!.notifyItemChanged(it) }
        viewModel.followResponse.observe(viewLifecycleOwner) { feedAdapter!!.refresh() }
        viewModel.errorNorException.observe(viewLifecycleOwner) { toast(it) }
    }

    override fun onDestroyView() {
        videoViewHolder?.stopPlayer()
        videoViewHolder?.releasePlayer()
        feedAdapter = null

        super.onDestroyView()
    }

    override fun onDestroy() {
        videoViewHolder?.stopPlayer()
        videoViewHolder?.releasePlayer()
        feedAdapter = null

        super.onDestroy()
    }

    override fun onStop() {
        videoViewHolder?.pausePlayer()

        super.onStop()
    }
}