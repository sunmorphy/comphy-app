package com.comphy.photo.ui.auth.forgot

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseAuthActivity
import com.comphy.photo.databinding.ActivityForgotPasswordBinding
import com.comphy.photo.ui.auth.register.RegisterActivity
import com.comphy.photo.ui.auth.verify.VerifyActivity
import com.comphy.photo.utils.Extension
import com.comphy.photo.utils.Extension.formatErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class ForgotPasswordActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtEmail)
        actionWidgets = listOf(binding.btnBack, binding.btnRegister, binding.btnSendEmail)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        errorWidgets = listOf(binding.txtErrorEmail)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_send

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = resources.getString(R.string.register_success_title)
            btnSheetAction.text = resources.getString(R.string.string_verify)
        }

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnSendEmail.setOnClickListener {
            setFieldError(false)

            if (isFieldEmpty()) {
                setFieldError(true, eachField = true)

            } else {
                lifecycleScope.launch { viewModel.userForgot(binding.edtEmail.text.toString()) }
            }
        }
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnRegister.setOnClickListener {
            start<RegisterActivity>()
            finish()
        }
        binding.btnDismissError.setOnClickListener { showError(false) }
    }

    override fun setupObserver() {
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.message.observe(this) {
            formatErrorMessage(it, binding.txtErrorTitle, binding.txtErrorDesc)
            setFieldError(true)
        }
        viewModel.exceptionResponse.observe(this) { if (it != null) toast(it) }
        viewModel.authResponse.observe(this) {
            val spanMessage = SpannableString(it)
            spanMessage.apply {
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            this@ForgotPasswordActivity,
                            R.color.neutral_black_30
                        )
                    ),
                    0,
                    41,
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
            isDismissed = true
            showBottomSheetDialog {
                start<VerifyActivity> {
                    putExtra("extra_source", "forgot")
                    putExtra("extra_email", binding.edtEmail.text.toString())
                }
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupClickListener()
    }
}