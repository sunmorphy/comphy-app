package com.comphy.photo.ui.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.anilokcun.uwmediapicker.model.UwMediaPickerMediaModel
import com.bumptech.glide.Glide
import com.chibatching.kotpref.bulk
import com.comphy.photo.R
import com.comphy.photo.data.source.local.sharedpref.auth.UserAuth
import com.comphy.photo.data.source.remote.response.user.detail.UserResponseData
import com.comphy.photo.databinding.*
import com.comphy.photo.helpers.ViewPagerSetupHelper
import com.comphy.photo.ui.auth.forgot.ForgotPasswordActivity
import com.comphy.photo.ui.auth.login.LoginActivity
import com.comphy.photo.ui.bookmark.BookmarkActivity
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.profile.fragment.FollowerFragment
import com.comphy.photo.ui.profile.fragment.FollowingFragment
import com.comphy.photo.ui.profile.fragment.ProfileAboutFragment
import com.comphy.photo.ui.profile.fragment.ProfilePostFragment
import com.comphy.photo.utils.Extension
import com.comphy.photo.utils.Extension.formatLocationInput
import com.comphy.photo.utils.Extension.loadAnim
import com.comphy.photo.utils.Extension.pagerAdapter
import com.comphy.photo.utils.Extension.sizeInMb
import com.comphy.photo.vo.CommunityImageType
import com.comphy.photo.vo.FollowType
import com.comphy.photo.vo.MediaSize
import com.comphy.photo.vo.UploadType
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.asRequestBody
import splitties.activities.start
import splitties.resources.color
import splitties.resources.colorSL
import splitties.resources.drawable
import splitties.toast.toast
import java.io.File
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.profile_tab_post,
            R.string.profile_tab_about
        )

        @StringRes
        private val FOLLOW_TAB_TITLES = intArrayOf(
            R.string.string_follower,
            R.string.string_following
        )

        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val EXTRA_ID = "extra_id"
    }

    val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val bottomSheetBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetBinding.inflate(layoutInflater)
    }
    private val bottomSheetLogoutBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetLogoutBinding.inflate(layoutInflater)
    }
    private val bottomSheetPrivacySecurityBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetPrivacySecurityBinding.inflate(layoutInflater)
    }
    private val bottomSheetChangePasswordBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetChangePasswordBinding.inflate(layoutInflater)
    }
    private val bottomSheetEditProfileBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetEditProfileBinding.inflate(layoutInflater)
    }
    private val bottomSheetFollowBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetFollowBinding.inflate(layoutInflater)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: ProfileViewModel by viewModels()
    val viewPagerSetupHelper by lazy(LazyThreadSafetyMode.NONE) { ViewPagerSetupHelper(this) }
    private val getPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            val isGranted = it.entries.all { it.value == true }
            if (!isGranted) toast("Comphy memerlukan izin anda")
        }

    @Inject
    lateinit var userAuth: UserAuth
    var extraId = -1

    private var observableMediaPath = MutableLiveData<MutableList<String?>>()
    private val imagesPath: MutableList<String?> = mutableListOf("", "")
    private var profileUrl: String? = ""
    private var bannerUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        extraId = intent.getIntExtra(EXTRA_ID, -1)

        if (extraId == -1) finish()
        else {
            setupObserver()

            lifecycleScope.launch {
                if (extraId == userAuth.userId) viewModel.getUserDetails()
                else viewModel.getUserDetailsById(extraId)
                viewModel.getUserFollowing(extraId)
                viewModel.getUserFollowers(extraId)
                viewModel.getCities()
            }

            viewPagerSetupHelper.setupNormal(
                binding.tabProfile,
                binding.vpTabProfile,
                pagerAdapter(listOf(ProfilePostFragment(), ProfileAboutFragment())),
                TAB_TITLES
            )
        }
    }

    private fun setupObserver() {
        viewModel.isFetching.observe(this) { customLoading.showLoading(it) }
        viewModel.isLoading.observe(this) { customLoading.showLoading(it) }
        viewModel.isUpdateUserDataLoading.observe(this) { showButtonUpdateLoading(it) }
        viewModel.userData.observe(this) { userData ->
            setupWidgets(userData)
            viewModel.uploadsUrl.observe(this) {
                viewModel.uploadFor.observe(this) { mFor ->
                    lifecycleScope.launch {
                        if (mFor == null) {
                            uploadImages(it[0].storageUrl, CommunityImageType.PROFILE)
                            uploadImages(it[1].storageUrl, CommunityImageType.BANNER)
                            profileUrl = it[0].storagePath
                            bannerUrl = it[1].storagePath
                            viewModel.updateUserDetails(
                                fullname = userData.fullname,
                                location = userData.location,
                                numberPhone = userData.numberPhone,
                                job = userData.job,
                                description = userData.description,
                                socialMedia = userData.socialMedia,
                                profilePhotoLink = profileUrl,
                                profileBannerLink = bannerUrl
                            )
                        } else {
                            uploadImages(it[0].storageUrl, mFor)
                            if (mFor == CommunityImageType.PROFILE) {
                                profileUrl = it[0].storagePath
                                bannerUrl = null
                            } else {
                                profileUrl = null
                                bannerUrl = it[1].storagePath
                            }
                            viewModel.updateUserDetails(
                                fullname = userData.fullname,
                                location = userData.location,
                                numberPhone = userData.numberPhone,
                                job = userData.job,
                                description = userData.description,
                                socialMedia = userData.socialMedia,
                                profilePhotoLink = profileUrl.takeIf { it != null }
                                    ?: userData.profilePhotoLink,
                                profileBannerLink = bannerUrl.takeIf { it != null }
                                    ?: userData.profileBannerLink
                            )
                        }
                    }
                }
            }
        }
        viewModel.updateResponse.observe(this) {
            bottomSheetEditProfileBinding.btnSaveChange.apply {
                backgroundTintList = colorSL(R.color.primary_green)
                text = getString(R.string.reset_success_title)
                isEnabled = false
            }
            lifecycleScope.launch {
                delay(500)
                bottomSheetDialog.dismiss()
                if (extraId == userAuth.userId) viewModel.getUserDetails()
                else viewModel.getUserDetailsById(extraId)
            }
        }
        viewModel.experienceResponse.observe(this) {
            lifecycleScope.launch {
                if (extraId == userAuth.userId) viewModel.getUserDetails()
                else viewModel.getUserDetailsById(extraId)
            }
        }
        viewModel.cities.observe(this) {
            val locationAdapter =
                ArrayAdapter(
                    this,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    Extension.formatCity(it)
                )

            bottomSheetEditProfileBinding.edtLocation.apply {
                setDropDownBackgroundDrawable(drawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
        viewModel.isChangePasswordLoading.observe(this) { showButtonChangePasswordLoading(it) }
        viewModel.successVerifyPasswordResponse.observe(this) {
            bottomSheetChangePasswordBinding.edtPassword.text = ""
            bottomSheetDialog.dismiss()
            showBottomSheetChangePassword()
        }
        viewModel.successChangePasswordResponse.observe(this) {
            bottomSheetDialog.dismiss()
            showBottomSheetChangePasswordSuccess()
        }
        viewModel.errorChangePasswordResponse.observe(this) {
            bottomSheetChangePasswordBinding.txtErrorPassword.text = it
            bottomSheetChangePasswordBinding.edtPassword.background =
                drawable(R.drawable.widget_error)
        }
    }

    private fun setupWidgets(userData: UserResponseData) {
        binding.btnBack.setOnClickListener { onBackPressed() }
        if (extraId == userAuth.userId) {
            profileUrl = userData.profilePhotoLink
            bannerUrl = userData.profileBannerLink
            imagesPath[0] = bannerUrl
            imagesPath[1] = profileUrl
            binding.btnLogout.apply {
                visibility = View.VISIBLE
                setOnClickListener { showBottomSheetLogout() }
            }
            binding.layoutUpgradeAccount.visibility =
                if (userData.subscription == null
                    || userData.subscription?.limitCreateCommunity!! <= 2
                    || userData.subscription?.limitJoinCommunity!! <= 8
                ) View.VISIBLE
                else View.GONE
            binding.btnAddUser.visibility = View.GONE
            binding.btnSettings.apply {
                visibility = View.VISIBLE
                setOnClickListener { showBottomSheetPrivacySecurity(userData) }
            }
            binding.btnEdit.apply {
                visibility = View.VISIBLE
                setOnClickListener { showBottomSheetEditProfile(userData) }
            }
            binding.btnToBookmark.apply {
                visibility = View.VISIBLE
                setOnClickListener { start<BookmarkActivity>() }
            }
            binding.imgBanner.setOnClickListener {
                requestAccessForFile {
                    openPicker {
                        imagesPath[0] = it.mediaPath
                        Glide.with(this)
                            .load(imagesPath[0])
                            .centerCrop()
                            .into(binding.imgBanner)
                        binding.txtBannerMax.setTextColor(color(R.color.white))
                        binding.txtBannerSize.setTextColor(color(R.color.white))
                        binding.imgBannerOverlay.visibility = View.VISIBLE
                        observableMediaPath.value = imagesPath
                        observeChange()
                    }
                }
            }
            binding.imgProfile.setOnClickListener {
                requestAccessForFile {
                    openPicker {
                        imagesPath[1] = it.mediaPath
                        Glide.with(this)
                            .load(imagesPath[1])
                            .centerCrop()
                            .into(binding.imgProfile)
                        observableMediaPath.value = imagesPath
                        observeChange()
                    }
                }
            }
        } else {
            binding.layoutUpgradeAccount.visibility = View.GONE
            binding.btnLogout.visibility = View.GONE
            binding.btnAddUser.apply {
                visibility = View.VISIBLE
                when (userData.isFollowed) {
                    FollowType.FOLLOWED -> {
                        binding.btnAddUser.apply {
                            text = getString(R.string.string_followed)
                            setTextColor(colorSL(R.color.primary_orange))
                            setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_remove_user,
                                0,
                                0,
                                0
                            )
                            setOnClickListener {
                                lifecycleScope.launch { viewModel.unfollowUser(userData.id) }
                                userData.isFollowed = FollowType.NOT_FOLLOWED
                                setButtonFollow(
                                    (userData.isFollowed == FollowType.FOLLOWED),
                                    userData
                                )
                            }
                        }
                    }
                    FollowType.NOT_FOLLOWED -> {
                        binding.btnAddUser.apply {
                            text = getString(R.string.string_follow)
                            setTextColor(colorSL(R.color.neutral_black))
                            setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.ic_add_user,
                                0,
                                0,
                                0
                            )
                            setOnClickListener {
                                lifecycleScope.launch { viewModel.followUser(userData.id) }
                                userData.isFollowed = FollowType.FOLLOWED
                                setButtonFollow(
                                    (userData.isFollowed == FollowType.FOLLOWED),
                                    userData
                                )
                            }
                        }
                    }
                }
            }
            binding.btnSettings.visibility = View.INVISIBLE
            binding.btnEdit.visibility = View.INVISIBLE
            binding.btnToBookmark.visibility = View.GONE
        }

        binding.layoutUserFollower.setOnClickListener { showBottomSheetFollow() }
        binding.layoutUserFollowing.setOnClickListener { showBottomSheetFollow() }
        binding.layoutUserPost.setOnClickListener { showBottomSheetFollow() }
        when {
            userData.profilePhotoLink != null -> {
                Glide.with(this)
                    .load(userData.profilePhotoLink)
                    .centerCrop()
                    .into(binding.imgProfile)
            }
            userData.profileBannerLink != null -> {
                Glide.with(this)
                    .load(userData.profileBannerLink)
                    .centerCrop()
                    .into(binding.imgBanner)
                binding.txtBannerMax.visibility = View.INVISIBLE
                binding.txtBannerSize.visibility = View.INVISIBLE
                binding.imgBannerOverlay.visibility = View.VISIBLE
            }
        }
        binding.txtUserName.text = userData.fullname
        binding.txtUserJob.text = userData.job
        binding.txtUserLocation.text = formatLocationInput(userData.location!!)
        binding.txtFollowerCount.text = userData.lengthFollowers.toString()
        binding.txtFollowingCount.text = userData.lengthFollowing.toString()
        binding.txtPostCount.text = userData.amountOfPost.toString()
    }

    private fun setButtonFollow(isFollowed: Boolean, userData: UserResponseData) {
        binding.btnAddUser.apply {
            if (isFollowed) {
                text = getString(R.string.string_followed)
                setCompoundDrawablesRelative(
                    null,
                    null,
                    drawable(R.drawable.ic_remove_user),
                    null
                )
                setOnClickListener {
                    lifecycleScope.launch { viewModel.followUser(userData.id) }
                    userData.isFollowed = FollowType.FOLLOWED
                }
            } else {
                text = getString(R.string.string_follow)
                setCompoundDrawablesRelative(
                    null,
                    null,
                    drawable(R.drawable.ic_add_user),
                    null
                )
                setOnClickListener {
                    lifecycleScope.launch { viewModel.unfollowUser(userData.id) }
                    userData.isFollowed = FollowType.NOT_FOLLOWED
                }
            }
        }
    }

    private fun showBottomSheetLogout() {
        with(bottomSheetLogoutBinding) {
            btnLogout.setOnClickListener {
                userAuth.bulk {
                    userAccessToken = null
                    userRefreshToken = null
                    userLoggedTime = -1
                    isLogin = false
                    isUserUpdated = false
                }
                bottomSheetDialog.dismiss()
                start<LoginActivity>()
                finishAffinity()
            }
            btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        }

        bottomSheetDialog.setContentView(bottomSheetLogoutBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetPrivacySecurity(userData: UserResponseData) {
        with(bottomSheetPrivacySecurityBinding) {
            btnChangePassword.setOnClickListener {
                bottomSheetDialog.dismiss()
                showBottomSheetVerifyPassword()
            }
        }
        bottomSheetDialog.setContentView(bottomSheetPrivacySecurityBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetVerifyPassword() {
        with(bottomSheetChangePasswordBinding) {
            btnForgotPassword.setOnClickListener { start<ForgotPasswordActivity>() }
            btnNext.setOnClickListener {
                if (edtPassword.text.isEmpty()) {
                    edtPassword.background = drawable(R.drawable.widget_error)
                    txtErrorPassword.visibility = View.VISIBLE
                } else {
                    edtPassword.background = drawable(R.drawable.state_field)
                    txtErrorPassword.visibility = View.INVISIBLE
                    lifecycleScope.launch {
                        viewModel.verifyPassword(edtPassword.text.toString())
                    }
                }
            }
        }
        bottomSheetDialog.setContentView(bottomSheetChangePasswordBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetChangePassword() {
        with(bottomSheetChangePasswordBinding) {
            txtChangePasswordDesc.text = getString(R.string.string_change_password_desc)
            txtYourPassword.text = getString(R.string.string_new_password)
            btnForgotPassword.setOnClickListener { start<ForgotPasswordActivity>() }
            btnNext.apply {
                text = getString(R.string.string_save)
                setOnClickListener {
                    if (edtPassword.text.isEmpty()) {
                        edtPassword.background = drawable(R.drawable.widget_error)
                        txtErrorPassword.visibility = View.VISIBLE
                    } else {
                        edtPassword.background = drawable(R.drawable.state_field)
                        txtErrorPassword.visibility = View.INVISIBLE

                        var newPassword = edtPassword.text.toString()
                        if (!isPasswordValid(newPassword.lowercase())
                            || newPassword.contains(" ")
                        ) newPassword = ""

                        lifecycleScope.launch {
                            viewModel.changePassword(newPassword)
                        }
                    }
                }
            }
        }
        bottomSheetDialog.setContentView(bottomSheetChangePasswordBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetEditProfile(userData: UserResponseData) {
        with(bottomSheetEditProfileBinding) {
            val inputWidgets = listOf(
                edtName,
                edtLocation,
                edtNumber,
                edtJob,
                edtDescription,
                edtMediaSocial
            )
            val requiredWidgets = listOf(
                edtName,
                edtLocation,
                edtJob,
                edtDescription
            )
            val editProfileField = listOf(
                userData.fullname,
                userData.location,
                userData.numberPhone,
                userData.job,
                userData.description,
                userData.socialMedia
            )
            edtName.text = userData.fullname
            edtLocation.setText(
                if (userData.location != null) formatLocationInput(userData.location!!)
                else null
            )
            edtNumber.setText(userData.numberPhone)
            edtJob.setText(userData.job)
            edtDescription.setText(userData.description)
            edtMediaSocial.setText(userData.socialMedia)

            fun isFieldNotNecessary(): Boolean {
                requiredWidgets.forEach { edt ->
                    if (edt.text.isEmpty()) return true
                }
                inputWidgets.forEach { edt ->
                    if (edt.text.toString() != editProfileField[inputWidgets.indexOf(edt)]) return false
                }
                return false
            }

            inputWidgets.forEach { edt ->
                edt.doAfterTextChanged { btnSaveChange.isEnabled = !isFieldNotNecessary() }
            }
            btnSaveChange.apply {
                backgroundTintList = colorSL(R.color.primary_orange)
                text = getString(R.string.string_save_change)
                setOnClickListener {
                    lifecycleScope.launch {
                        viewModel.updateUserDetails(
                            fullname = edtName.text.toString(),
                            location = edtLocation.text.toString().split(",")[0],
                            numberPhone = edtNumber.text.toString(),
                            job = edtJob.text.toString(),
                            description = edtDescription.text.toString(),
                            socialMedia = edtMediaSocial.text.toString(),
                            profilePhotoLink = userData.profilePhotoLink,
                            profileBannerLink = userData.profileBannerLink
                        )
                    }
                }
            }
        }
        bottomSheetDialog.setContentView(bottomSheetEditProfileBinding.root)
        bottomSheetDialog.show()
    }

    private fun showBottomSheetFollow() {
        with(bottomSheetFollowBinding) {
            viewPagerSetupHelper.setupNormal(
                tabFollow,
                vpTabFollow,
                pagerAdapter(listOf(FollowerFragment(), FollowingFragment())),
                FOLLOW_TAB_TITLES
            )
        }
        bottomSheetDialog.setContentView(bottomSheetFollowBinding.root)
        bottomSheetDialog.show()
    }

    private fun showButtonUpdateLoading(state: Boolean) {
        with(bottomSheetEditProfileBinding) {
            val inputWidgets = listOf(
                edtName,
                edtLocation,
                edtNumber,
                edtJob,
                edtDescription,
                edtMediaSocial
            )
            if (state) {
                btnSaveChange.apply {
                    text = null
                    isEnabled = false
                }
                inputWidgets.forEach { it.isEnabled = false }
                imgLoadingBtn.apply {
                    visibility = View.VISIBLE
                    startAnimation(loadAnim(R.anim.btn_loading_anim))
                }
                bottomSheetDialog.setCancelable(false)
            } else {
                btnSaveChange.apply {
                    text = getString(R.string.string_save_change)
                    isEnabled = true
                }
                inputWidgets.forEach { it.isEnabled = true }
                imgLoadingBtn.apply {
                    clearAnimation()
                    visibility = View.GONE
                }
                bottomSheetDialog.setCancelable(true)
            }
        }
    }

    private fun showButtonChangePasswordLoading(state: Boolean) {
        with(bottomSheetChangePasswordBinding) {
            if (state) {
                btnNext.apply {
                    text = null
                    isEnabled = false
                }
                btnForgotPassword.isEnabled = false
                edtPassword.isEnabled = false
                imgLoadingBtn.apply {
                    visibility = View.VISIBLE
                    startAnimation(loadAnim(R.anim.btn_loading_anim))
                }
                bottomSheetDialog.setCancelable(false)
            } else {
                btnNext.apply {
                    text = getString(R.string.string_save_change)
                    isEnabled = true
                }
                btnForgotPassword.isEnabled = true
                edtPassword.isEnabled = true
                imgLoadingBtn.apply {
                    clearAnimation()
                    visibility = View.GONE
                }
                bottomSheetDialog.setCancelable(true)
            }
        }
    }

    private fun showBottomSheetChangePasswordSuccess() {
        with(bottomSheetBinding) {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = getString(R.string.string_success_change_password)
            txtSheetDesc.text = getString(R.string.string_success_change_password_desc)
            btnSheetAction.apply {
                text = getString(R.string.string_ok)
                setOnClickListener { bottomSheetDialog.dismiss() }
            }
        }
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z]).{6,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)

        return matcher.matches()
    }

    private fun requestAccessForFile(grantedAction: () -> Unit) {
        if (PERMISSIONS.all {
                ContextCompat.checkSelfPermission(
                    this, it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            getPermissionResult.launch(PERMISSIONS)

        } else {
            grantedAction()
        }
    }

    private fun openPicker(selectedMedia: (UwMediaPickerMediaModel) -> Unit) {
        UwMediaPicker.with(this)
            .setGalleryMode(UwMediaPicker.GalleryMode.ImageGallery)
            .setGridColumnCount(2)
            .setMaxSelectableMediaCount(1)
            .setLightStatusBar(true)
            .launch {
                it?.forEach { item ->
                    val file = File(item.mediaPath)
                    if (file.sizeInMb <= MediaSize.IMAGE_NON_POST) {
                        selectedMedia(item)
                    } else {
                        toast("File harus lebih kecil dari ${MediaSize.IMAGE_NON_POST.toInt()}MB")
                    }
                }
            }
    }

    private suspend fun uploadImages(url: String, imageType: CommunityImageType) {
        when (imageType) {
            CommunityImageType.PROFILE -> {
                val reqFile = imagesPath[1]?.let { File(it).asRequestBody() }
                if (reqFile != null) {
                    viewModel.uploadImageNonPost(url, reqFile)
                }
            }
            CommunityImageType.BANNER -> {
                val reqFile = imagesPath[0]?.let { File(it).asRequestBody() }
                if (reqFile != null) {
                    viewModel.uploadImageNonPost(url, reqFile)
                }
            }
        }
    }

    private fun observeChange() {
        observableMediaPath.observe(this) {
            it.forEach { url ->
                if (url == null || url.lowercase().contains("https") || url.isEmpty()) {
                    binding.btnApplyChange.visibility = View.GONE
                } else {
                    val listPath = mutableListOf<String>()
                    var selectedType = CommunityImageType.NONE
                    imagesPath.forEach { path ->
                        if (path != null && path.isNotEmpty()) {
                            listPath.add(path)
                            selectedType = if (path == imagesPath[0]) CommunityImageType.BANNER
                            else CommunityImageType.PROFILE
                        }
                    }
                    binding.btnApplyChange.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            lifecycleScope.launch {
                                if (listPath.size == 1) {
                                    viewModel.getUploadLink(
                                        UploadType.IMAGE,
                                        listPath.size,
                                        selectedType
                                    )
                                } else {
                                    viewModel.getUploadLink(
                                        UploadType.IMAGE,
                                        listPath.size
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}