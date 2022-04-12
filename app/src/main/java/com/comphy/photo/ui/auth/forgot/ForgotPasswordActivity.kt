package com.comphy.photo.ui.auth.forgot

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityForgotPasswordBinding
import com.comphy.photo.ui.auth.register.RegisterActivity
import com.comphy.photo.ui.auth.verify.VerifyActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class ForgotPasswordActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtEmail)
        actionWidgets = listOf(binding.btnBack, binding.btnRegister, binding.btnSendEmail)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.forgot_send

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = resources.getString(R.string.register_success_title)
            btnSheetAction.text = resources.getString(R.string.string_verify)
        }
    }

    override fun setupClickListener() {
        binding.btnSendEmail.setOnClickListener {
            setFieldError(false)
            lifecycleScope.launch { viewModel.userForgot(binding.edtEmail.text.toString()) }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnRegister.setOnClickListener { start<RegisterActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
    }

    override fun setupObserver() {
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.message.observe(this) {
            val errMessage = it.split("\n")
            binding.txtErrorTitle.text = errMessage[0]
            binding.txtErrorDesc.text = errMessage[1]
            setFieldError(true)
        }
        viewModel.authResponse.observe(this) {
            val spanMessage = SpannableString(it)
            spanMessage.apply {
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    41,
                    it.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            this@ForgotPasswordActivity,
                            R.color.neutral_black
                        )
                    ),
                    41,
                    it.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            bottomSheetBinding.txtSheetDesc.text = spanMessage
            showBottomSheetDialog {
                start<VerifyActivity> {
                    putExtra("extra_source", "forgot")
                    putExtra("extra_email", binding.edtEmail.text.toString())
                }
            }
        }
    }
}