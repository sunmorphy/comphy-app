package com.comphy.photo.ui.auth.reset

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityResetPasswordBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import com.comphy.photo.ui.auth.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class ResetPasswordActivity : BaseAuthActivity() {
    companion object {
        private const val EXTRA_OTP = "extra_otp"
        private const val EXTRA_EMAIL = "extra_email"
    }

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var otpExtra: String
    private lateinit var emailExtra: String
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        otpExtra = intent.getStringExtra(EXTRA_OTP)!!
        emailExtra = intent.getStringExtra(EXTRA_EMAIL)!!

        inputWidgets = listOf(binding.edtPassword, binding.edtConfirmPassword)
        actionWidgets = listOf(binding.btnBack, binding.btnRegister, binding.btnSave)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_verify

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = resources.getString(R.string.reset_success_title)
            btnSheetAction.text = resources.getString(R.string.string_login)
        }
    }

    override fun setupClickListener() {
        binding.btnSave.setOnClickListener {
            setFieldError(false)
            lifecycleScope.launch {
                if (binding.edtConfirmPassword.text.toString() == binding.edtPassword.text.toString()) {
                    viewModel.userForgotReset(
                        otpExtra,
                        binding.edtPassword.text.toString(),
                        emailExtra
                    )
                } else {
                    binding.txtErrorTitle.text =
                        resources.getString(R.string.reset_confirm_error_title)
                    binding.txtErrorDesc.text =
                        resources.getString(R.string.reset_confirm_error_description)
                    setFieldError(true)
                }
            }
        }

        binding.btnRegister.setOnClickListener { start<RegisterActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
        binding.btnBack.setOnClickListener { onBackPressed() }
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
            bottomSheetBinding.txtSheetDesc.text = it
            showBottomSheetDialog { start<LoginActivity>() }
        }
    }
}