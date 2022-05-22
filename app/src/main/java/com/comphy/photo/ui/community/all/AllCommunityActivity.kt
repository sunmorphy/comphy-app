package com.comphy.photo.ui.community.all

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityAllCommunityBinding
import com.comphy.photo.databinding.BottomSheetCommunitySetBinding
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class AllCommunityActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_OWN = "extra_own"
        private const val EXTRA_FOLLOWED = "extra_followed"
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityAllCommunityBinding.inflate(layoutInflater)
    }
    private val bottomSheetOptionBinding by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetCommunitySetBinding.inflate(layoutInflater)
    }
    private val bottomSheetDialog by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
    }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: AllCommunityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val ownExtra = intent.getStringExtra(EXTRA_OWN)
        val followedExtra = intent.getStringExtra(EXTRA_FOLLOWED)

        lifecycleScope.launch {
            when {
                ownExtra != null -> {
                    viewModel.getCreatedCommunities()
                    viewModel.userCreatedCommunity.observe(this@AllCommunityActivity) {
                        binding.rvCommunity.apply {
                            layoutManager = LinearLayoutManager(this@AllCommunityActivity)
                            adapter = AllCommunityAdapter(it) { communityId ->
                                bottomSheetOptionBinding.apply {
                                    txtCommunitySettings.apply {
                                        visibility = View.VISIBLE
                                        setOnClickListener { toast("COMMUNITY SETTING NOT SETTED YET") }
                                    }
                                    divider.visibility = View.VISIBLE
                                    txtCommunityLeave.setOnClickListener {
                                        lifecycleScope.launch { viewModel.leaveCommunity(communityId) }
                                    }
                                }
                                bottomSheetDialog.setContentView(bottomSheetOptionBinding.root)
                                bottomSheetDialog.show()
                            }
                        }
                    }
                }
                followedExtra != null -> {
                    viewModel.getJoinedCommunities()
                    viewModel.userJoinedCommunity.observe(this@AllCommunityActivity) {
                        binding.rvCommunity.apply {
                            layoutManager = LinearLayoutManager(this@AllCommunityActivity)
                            adapter = AllCommunityAdapter(it) { communityId ->
                                bottomSheetOptionBinding.apply {
                                    txtCommunitySettings.visibility = View.GONE
                                    divider.visibility = View.GONE
                                    txtCommunityLeave.setOnClickListener {
                                        lifecycleScope.launch { viewModel.leaveCommunity(communityId) }
                                    }
                                }
                                bottomSheetDialog.setContentView(bottomSheetOptionBinding.root)
                                bottomSheetDialog.show()
                            }
                        }
                    }
                }
            }
        }

        viewModel.isFetching.observe(this) { if (it) customLoading.show() else customLoading.dismiss() }
        viewModel.exceptionResponse.observe(this) { toast(it) }
        viewModel.leaveResponse.observe(this) {
            bottomSheetDialog.dismiss()
            start<MainActivity>()
            finish()
        }
    }
}