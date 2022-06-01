package com.comphy.photo.ui.community.edit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.data.source.remote.response.community.follow.FollowCommunityResponseContentItem
import com.comphy.photo.databinding.BottomSheetCommunityCodeBinding
import com.comphy.photo.databinding.FragmentEditCommunityProfileBinding
import com.comphy.photo.ui.community.edit.EditCommunityActivity
import com.comphy.photo.ui.community.edit.EditCommunityViewModel
import com.comphy.photo.utils.Extension
import com.comphy.photo.utils.Extension.formatLocationInput
import com.comphy.photo.vo.CommunityImageType
import com.comphy.photo.vo.UploadType
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.asRequestBody
import splitties.resources.drawable
import splitties.toast.toast
import java.io.File

class EditCommunityProfileFragment : Fragment() {

    private var _binding: FragmentEditCommunityProfileBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext(), R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetCommunityCodeBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetCommunityCodeBinding.inflate(layoutInflater)
    }
    private val viewModel: EditCommunityViewModel by activityViewModels()
    private var contentItem: FollowCommunityResponseContentItem? = null
    private lateinit var newImagesPath: MutableList<String>
    private var newProfilePhotoUrl: String? = null
    private var newBannerPhotoUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditCommunityProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newImagesPath = (activity as EditCommunityActivity).imagesPath
        newProfilePhotoUrl = (activity as EditCommunityActivity).profileUrl
        newBannerPhotoUrl = (activity as EditCommunityActivity).bannerUrl

        setupObserver()

        lifecycleScope.launch { viewModel.getCities() }

        contentItem = (activity as EditCommunityActivity).contentItem
        if (contentItem != null) setupWidgets()
    }

    private fun setupObserver() {
        viewModel.cities.observe(viewLifecycleOwner) {
            val locationAdapter =
                ArrayAdapter(
                    requireContext(),
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    Extension.formatCity(it)
                )

            binding.edtCommunityLocation.apply {
                setDropDownBackgroundDrawable(drawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
        viewModel.uploadsUrl.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                uploadImages(it[0].storageUrl, CommunityImageType.PROFILE)
                uploadImages(it[1].storageUrl, CommunityImageType.BANNER)
                newProfilePhotoUrl = it[0].storagePath
                newBannerPhotoUrl = it[1].storagePath
            }
        }
    }

    private fun setupWidgets() {
        contentItem?.let {
            if (!isPrivateCommunity()) {
                binding.txtCommunityCode.isEnabled = false
                binding.txtPrivateCommunityCode.apply {
                    isEnabled = false
                    text = getString(R.string.ec_community_code_empty)
                }
                binding.btnCopyPrivateCommunityCode.isEnabled = false
            } else {
                binding.txtCommunityCode.isEnabled = true
                binding.txtPrivateCommunityCode.apply {
                    isEnabled = true
                    text = it.communityCode.toString()
                    setOnClickListener { bottomSheetCommunityCodeBinding }
                }
                binding.btnCopyPrivateCommunityCode.isEnabled = true
            }
            binding.edtCommunityName.setText(it.communityName)
            binding.edtCommunityLocation.setText(formatLocationInput(it.location))
            binding.edtCommunityDescription.setText(it.description)

            if (it.communityType.lowercase().contains("pub")) binding.rbCommunityPublic.isChecked = true
            else binding.rbCommunityPrivate.isChecked = true

            binding.btnSaveChange.setOnClickListener { _ ->
                val listPath = mutableListOf<String>()
                newImagesPath.forEach { path -> if (path.isNotEmpty()) listPath.add(path) }

                toast(listPath.size)
                toast((newProfilePhotoUrl == contentItem!!.profilePhotoCommunityLink).toString())
                toast((newBannerPhotoUrl == contentItem!!.bannerPhotoCommunityLink).toString())

//                lifecycleScope.launch {
//                    if (listPath.isEmpty()) editCommunity()
//                    else viewModel.getUploadLink(UploadType.IMAGE, listPath.size)
//                }
            }
        }
    }

    private suspend fun uploadImages(url: String, imageType: CommunityImageType) {
        when (imageType) {
            CommunityImageType.PROFILE -> {
                val reqFile = File(newImagesPath[1]).asRequestBody()
                viewModel.uploadImageNonPost(url, reqFile)
            }
            CommunityImageType.BANNER -> {
                val reqFile = File(newImagesPath[0]).asRequestBody()
                viewModel.uploadImageNonPost(url, reqFile)
            }
        }
    }

    private suspend fun editCommunity() {
        viewModel.editCommunity(
            communityName = binding.edtCommunityName.text.toString()
                .ifEmpty { contentItem!!.communityName },
            description = binding.edtCommunityDescription.text.toString()
                .ifEmpty { contentItem!!.description },
            location = binding.edtCommunityLocation.text.toString()
                .split(",")[0].ifEmpty { contentItem!!.location },
            communityType = binding.rgCommunityType
                .findViewById<RadioButton>(binding.rgCommunityType.checkedRadioButtonId).text.toString(),
            profilePhotoCommunityLink = newProfilePhotoUrl,
            bannerPhotoCommunityLink = newBannerPhotoUrl
        )
    }

//    private fun setupBottomSheetChangeCommunityCode() {
//        val codeResult =
//            bottomSheetCommunityCodeBinding.edtCode1.text.toString() + bottomSheetCommunityCodeBinding.edtCode2.text.toString() + bottomSheetCommunityCodeBinding.edtCode3.text.toString() + bottomSheetCommunityCodeBinding.edtCode4.text.toString()
//        bottomSheetCommunityCodeBinding.
//
//        bottomSheetDialog.setContentView(bottomSheetCommunityCodeBinding.root)
//        bottomSheetDialog.show()
//    }

    private fun isPrivateCommunity(): Boolean = contentItem!!.communityCode != null
}