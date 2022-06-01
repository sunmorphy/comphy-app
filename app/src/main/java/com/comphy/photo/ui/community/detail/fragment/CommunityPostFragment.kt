package com.comphy.photo.ui.community.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.comphy.photo.R
import com.comphy.photo.databinding.FragmentCommunityPostBinding
import com.comphy.photo.ui.community.detail.CommunityDetailActivity
import com.comphy.photo.ui.community.detail.CommunityDetailViewModel
import com.comphy.photo.ui.community.detail.adapter.CommunitySimilarAdapter
import com.comphy.photo.ui.community.edit.EditCommunityActivity
import com.comphy.photo.ui.main.fragment.feed.FeedAdapter
import com.comphy.photo.ui.post.CreatePostActivity
import kotlinx.coroutines.launch
import splitties.fragments.start
import splitties.resources.drawable
import splitties.toast.toast

class CommunityPostFragment : Fragment() {

    private var _binding: FragmentCommunityPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CommunityDetailViewModel by activityViewModels()
    private var feedAdapter: FeedAdapter? = null
    private var communitySimilarAdapter: CommunitySimilarAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentItem = (activity as CommunityDetailActivity).contentItem

        lifecycleScope.launch {
            viewModel.getCommunityPosts(contentItem!!.id)
            viewModel.getSimilarCommunity(contentItem.categoryCommunity.id)
        }

        Glide.with(this)
            .load(contentItem!!.profilePhotoCommunityLink)
            .placeholder(drawable(R.drawable.ic_placeholder_people))
            .error(drawable(R.drawable.ic_placeholder_people))
            .centerCrop()
            .into(binding.imgAdminProfile)

        binding.txtCommunitySettings.setOnClickListener {
            start<EditCommunityActivity> {
                putExtra("extra_content_item", (activity as CommunityDetailActivity).contentItem)
            }
        }
        setupObserver()
        setupWidgets()
    }

    private fun setupObserver() {
        viewModel.communityPosts.observe(viewLifecycleOwner) {
            lifecycleScope.launch { feedAdapter?.submitData(it) }
        }
        viewModel.similarCommunity.observe(viewLifecycleOwner) {
            communitySimilarAdapter = CommunitySimilarAdapter(it) {
                start<CommunityDetailActivity> {
                    putExtra("extra_detail", it)
                }
            }
        }
    }

    private fun setupWidgets() {
        if (feedAdapter?.itemCount == 0) {
            binding.txtUserCreateCommunity.visibility = View.VISIBLE
            binding.layoutCommunityCreated.visibility = View.VISIBLE
            binding.txtCommunitySettings.setOnClickListener {
                start<EditCommunityActivity> {
                    putExtra("extra_content_item", (activity as CommunityDetailActivity).contentItem)
                }
            }
        } else {
            binding.txtUserCreateCommunity.visibility = View.GONE
            binding.layoutCommunityCreated.visibility = View.GONE
            binding.rvCommunityPost.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = feedAdapter
            }
        }
        binding.rvSimilarCommunity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = communitySimilarAdapter
        }
        binding.btnSeeAllSimilar.setOnClickListener {
            // TODO: INTENT TO SEE ALL COMMUNITY BUT SIMILAR
            toast("THIS FEATURE IS NOT READY YET")
        }
        binding.btnDismiss.setOnClickListener { binding.layoutSimilarCommunity.visibility = View.GONE }
        binding.edtWritePost.setOnClickListener {
            start<CreatePostActivity> { putExtra("extra_text", "extra_text") }
        }
        binding.btnUploadImage.setOnClickListener {
            start<CreatePostActivity> { putExtra("extra_image", "extra_image") }
        }
        binding.btnUploadVideo.setOnClickListener {
            start<CreatePostActivity> { putExtra("extra_video", "extra_video") }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        communitySimilarAdapter = null
    }
}