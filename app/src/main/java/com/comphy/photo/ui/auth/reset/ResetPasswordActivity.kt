package com.comphy.photo.ui.auth.reset

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseAuthActivity
import com.comphy.photo.databinding.ActivityResetPasswordBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import com.comphy.photo.ui.auth.register.RegisterActivity
import com.comphy.photo.utils.Extension.formatErrorMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class ResetPasswordActivity : BaseAuthActivity() {
    companion object {
        private const val EXTRA_OTP = "extra_otp"
        private const val EXTRA_EMAIL = "extra_email"
    }

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var otpExtra: String
    private lateinit var emailExtra: String
    private lateinit var password: String
    private lateinit var confirmPassword: String
    private val viewModel: ResetPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        otpExtra = intent.getStringExtra(EXTRA_OTP)!!
        emailExtra = intent.getStringExtra(EXTRA_EMAIL)!!

        inputWidgets = listOf(binding.edtPassword, binding.edtConfirmPassword)
        actionWidgets = listOf(binding.btnBack, binding.btnRegister, binding.btnSave)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        errorWidgets = listOf(binding.txtErrorPassword)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_verify

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = resources.getString(R.string.reset_success_title)
            btnSheetAction.text = resources.getString(R.string.string_login)
        }

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnSave.setOnClickListener {
            setFieldError(false)
            password = binding.edtPassword.text.toString()
            confirmPassword = binding.edtConfirmPassword.text.toString()

            if (password.isEmpty()) {
                binding.edtPassword.background =
                    ContextCompat.getDrawable(this, R.drawable.widget_error)
                binding.txtErrorPassword.visibility = View.VISIBLE

            } else {
                if (password == confirmPassword) {
                    lifecycleScope.launch {
                        viewModel.userForgotReset(otpExtra, password, emailExtra)
                    }
                } else {
                    binding.txtErrorTitle.text =
                        resources.getString(R.string.reset_confirm_error_title)
                    binding.txtErrorDesc.text =
                        resources.getString(R.string.reset_confirm_error_description)
                    setFieldError(true)
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            start<RegisterActivity>()
            finish()
        }
        binding.btnDismissError.setOnClickListener { showError(false) }
        binding.btnBack.setOnClickListener {
            start<LoginActivity>()
            finish()
        }
    }

    override fun setupObserver() {
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.message.observe(this) {
            formatErrorMessage(it, binding.txtErrorTitle, binding.txtErrorDesc)
            setFieldError(true)
        }
        viewModel.exceptionResponse.observe(this) { if (it != null) toast(it) }
        viewModel.authResponse.observe(this) {
            bottomSheetBinding.txtSheetDesc.text = it
            isDismissed = true
            showBottomSheetDialog {
                start<LoginActivity> {
                    putExtra("extra_email", emailExtra)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupClickListener()
    }
}