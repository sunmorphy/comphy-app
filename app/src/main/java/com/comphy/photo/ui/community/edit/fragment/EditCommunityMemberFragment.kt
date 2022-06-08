package com.comphy.photo.ui.community.edit.fragment

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.R
import com.comphy.photo.databinding.BottomSheetRemoveMemberBinding
import com.comphy.photo.databinding.FragmentEditCommunityMemberBinding
import com.comphy.photo.ui.community.edit.EditCommunityActivity
import com.comphy.photo.ui.community.edit.EditCommunityMemberAdapter
import com.comphy.photo.ui.community.edit.EditCommunityViewModel
import com.comphy.photo.utils.Extension.loadAnim
import kotlinx.coroutines.launch
import splitties.resources.color

class EditCommunityMemberFragment : Fragment() {

    private var _binding: FragmentEditCommunityMemberBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditCommunityViewModel by activityViewModels()
    private val bottomSheetRemoveMemberBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetRemoveMemberBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditCommunityMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getCommunityMembers((activity as EditCommunityActivity).contentItem!!.id)
        }

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.communityMembers.observe(viewLifecycleOwner) {
            binding.rvCommunityMembers.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = EditCommunityMemberAdapter(it) { userId, username ->
                    setupBottomSheetBanUser(
                        userId = userId,
                        communityId = (activity as EditCommunityActivity).contentItem!!.id,
                        username = username
                    )
                }
            }
        }
        viewModel.isBanLoading.observe(viewLifecycleOwner) {
            bottomSheetRemoveMemberBinding.apply {
                if (it) {
                    btnDelete.visibility = View.INVISIBLE
                    imgLoadingBtn.apply {
                        visibility = View.VISIBLE
                        startAnimation(activity?.loadAnim(R.anim.btn_loading_anim))
                    }
                    (activity as EditCommunityActivity).bottomSheetDialog.setCancelable(false)
                } else {
                    btnDelete.visibility = View.VISIBLE
                    imgLoadingBtn.apply {
                        clearAnimation()
                        visibility = View.GONE
                    }
                    (activity as EditCommunityActivity).bottomSheetDialog.setCancelable(true)
                }
                listOf(btnCancel, btnDelete).forEach { btn -> btn.isEnabled = !it }
            }
        }
        viewModel.banResponse.observe(viewLifecycleOwner) {
            val spannedDesc = SpannableString(
                String.format(getString(R.string.ec_community_ban_success_desc), it)
            )
            spannedDesc.apply {
                setSpan(
                    ForegroundColorSpan(color(R.color.neutral_black_30)),
                    0,
                    (getString(R.string.ec_community_ban_success_desc).length - it.length),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(color(R.color.neutral_black)),
                    (getString(R.string.ec_community_ban_success_desc).length - it.length),
                    String.format(getString(R.string.ec_community_ban_success_desc), it).length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            (activity as EditCommunityActivity).bottomSheetBinding.apply {
                animView.setAnimation(R.raw.anim_success)
                txtSheetTitle.text = getString(R.string.string_success_ban)
                txtSheetDesc.text = spannedDesc
                btnSheetAction.apply {
                    text = getString(R.string.string_ok)
                    setOnClickListener {
                        (activity as EditCommunityActivity).bottomSheetDialog.dismiss()
                    }
                }
            }
            (activity as EditCommunityActivity).bottomSheetDialog.setContentView(
                (activity as EditCommunityActivity).bottomSheetBinding.root
            )
            (activity as EditCommunityActivity).bottomSheetDialog.show()
        }
    }

    private fun setupBottomSheetBanUser(userId: Int, communityId: Int, username: String) {
        bottomSheetRemoveMemberBinding.btnCancel.setOnClickListener {
            (activity as EditCommunityActivity).bottomSheetDialog.dismiss()
        }
        bottomSheetRemoveMemberBinding.btnDelete.setOnClickListener {
            lifecycleScope.launch {
                viewModel.banUserCommunity(
                    userId = userId,
                    communityId = communityId,
                    userName = username
                )
            }
        }

        (activity as EditCommunityActivity).bottomSheetDialog.setContentView(
            bottomSheetRemoveMemberBinding.root
        )
        (activity as EditCommunityActivity).bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        (activity as EditCommunityActivity).viewPagerSetupHelper.setProperHeightOfView(binding.root)
    }
}