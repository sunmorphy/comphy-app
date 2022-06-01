package com.comphy.photo.ui.community.edit.fragment

import android.os.Bundle
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
import com.comphy.photo.ui.community.edit.EditCommunityMemberAdapter
import com.comphy.photo.ui.community.edit.EditCommunityViewModel
import com.comphy.photo.utils.Extension.loadAnim
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class EditCommunityMemberFragment : Fragment() {

    private var _binding: FragmentEditCommunityMemberBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditCommunityViewModel by activityViewModels()
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetTheme)
    }
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

        binding.rvCommunityMembers.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = EditCommunityMemberAdapter { setupBottomSheetBanUser(it) }
        }

        viewModel.isBanLoading.observe(viewLifecycleOwner) {
            bottomSheetRemoveMemberBinding.apply {
                if (it) {
                    btnDelete.visibility = View.INVISIBLE
                    imgLoadingBtn.apply {
                        visibility = View.VISIBLE
                        startAnimation(activity?.loadAnim(R.anim.btn_loading_anim))
                    }
                    bottomSheetDialog.setCancelable(false)
                } else {
                    btnDelete.visibility = View.VISIBLE
                    imgLoadingBtn.apply {
                        clearAnimation()
                        visibility = View.GONE
                    }
                    bottomSheetDialog.setCancelable(true)
                }
                listOf(btnCancel, btnDelete).forEach { btn -> btn.isEnabled = !it }
            }
        }
    }

    private fun setupBottomSheetBanUser(id: Int) {
        bottomSheetRemoveMemberBinding.btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetRemoveMemberBinding.btnDelete.setOnClickListener {
            lifecycleScope.launch { viewModel.banUserCommunity(id, "", id) }
        }

        bottomSheetDialog.setContentView(bottomSheetRemoveMemberBinding.root)
        bottomSheetDialog.show()
    }
}