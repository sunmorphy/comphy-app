package com.comphy.photo.ui.verify

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.DigitsKeyListener
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityVerifyBinding
import com.comphy.photo.ui.login.LoginActivity
import com.comphy.photo.ui.reset.ResetPasswordActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class VerifyActivity : BaseAuthActivity() {
    companion object {
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_REGISTER = "register"
        private const val EXTRA_FORGOT = "forgot"
        private const val EXTRA_EMAIL = "extra_email"
    }

    private lateinit var binding: ActivityVerifyBinding
    private lateinit var sourceExtra: String
    private lateinit var emailExtra: String
    private var statusCode: Int = 0
    private val viewModel: VerifyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sourceExtra = intent.getStringExtra(EXTRA_SOURCE)!!
        emailExtra = intent.getStringExtra(EXTRA_EMAIL)!!

        inputWidgets = listOf(binding.edtOtp1, binding.edtOtp2, binding.edtOtp3, binding.edtOtp4)
        actionWidgets = listOf(binding.btnLogin, binding.btnVerify)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_verify

        inputEachField()

        val verifySend = resources.getString(R.string.verify_send) + " "
        val spanVerifySend = SpannableString(verifySend + emailExtra)
        spanVerifySend.apply {
            setSpan(
                StyleSpan(Typeface.BOLD),
                verifySend.length,
                (verifySend + emailExtra).length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@VerifyActivity,
                        R.color.neutral_black
                    )
                ),
                verifySend.length,
                (verifySend + emailExtra).length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.txtVerifySend.text = spanVerifySend

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = resources.getString(R.string.verify_success_title)
            txtSheetDesc.text = resources.getString(R.string.verify_success_description)
            when (sourceExtra) {
                EXTRA_REGISTER -> {
                    btnSheetAction.text = resources.getString(R.string.verify_to_profile)
                }
                EXTRA_FORGOT -> {
                    btnSheetAction.text = resources.getString(R.string.verify_to_reset)
                }
            }
        }

        binding.btnVerify.setOnClickListener {
            setFieldError(false)
            lifecycleScope.launch {
                val otpResult =
                    binding.edtOtp1.text.toString() + binding.edtOtp2.text.toString() + binding.edtOtp3.text.toString() + binding.edtOtp4.text.toString()

                when (sourceExtra) {
                    EXTRA_REGISTER -> {
                        // TODO VERIFY REGISTER CODE
//                        viewModel.userRegisterVerify(otpResult, emailExtra) {
//                            if (statusCode == 200) {
//                                showBottomSheetDialog {
//                                    start<LoginActivity>()
//                                }
//                            } else {
//                                setFieldError(true)
//                            }
//                        }
                    }
                    EXTRA_FORGOT -> {
                        viewModel.userForgotVerify(otpResult, emailExtra) {
                            if (statusCode == 200) {
                                showBottomSheetDialog {
                                    start<ResetPasswordActivity> {
                                        putExtra("extra_email", emailExtra)
                                        putExtra("extra_otp", otpResult)
                                    }
                                }
                            } else {
                                setFieldError(true)
                            }
                        }
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener { start<LoginActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
        binding.btnResendCode.setOnClickListener {
            Toast.makeText(
                this@VerifyActivity,
                "Resend Button Pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun setupObserver() {
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.statusCode.observe(this) { statusCode = it }
        viewModel.message.observe(this) {
            if (statusCode != 200) {
                val errMessage = it.split("\n")
                binding.txtErrorTitle.text = errMessage[0]
                binding.txtErrorDesc.text = errMessage[1]
            }
        }
    }

    private fun inputEachField() {
        inputWidgets.forEach {
            it.keyListener = DigitsKeyListener.getInstance("0123456789")
            it.doAfterTextChanged { s ->
                if (s?.length == 1) {
                    val itIndex = inputWidgets.indexOf(it)
                    if (itIndex < inputWidgets.size - 1) {
                        inputWidgets[itIndex + 1].requestFocus()
                    }
                }
            }
        }
    }
}