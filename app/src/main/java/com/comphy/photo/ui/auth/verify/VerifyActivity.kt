package com.comphy.photo.ui.auth.verify

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.method.DigitsKeyListener
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseAuthActivity
import com.comphy.photo.databinding.ActivityVerifyBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import com.comphy.photo.ui.auth.reset.ResetPasswordActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class VerifyActivity : BaseAuthActivity() {
    companion object {
        private const val EXTRA_SOURCE = "extra_source"
        private const val EXTRA_REGISTER = "register"
        private const val EXTRA_FORGOT = "forgot"
        private const val EXTRA_NAME = "extra_name"
        private const val EXTRA_EMAIL = "extra_email"
        private const val EXTRA_PASSWORD = "extra_password"
        private const val EXTRA_TOKEN = "extra_token"
    }

    private lateinit var binding: ActivityVerifyBinding
    private lateinit var resendCodeTimer: CountDownTimer
    private lateinit var sourceExtra: String
    private lateinit var emailExtra: String
    private lateinit var otpResult: String
    private var nameExtra: String? = null
    private var passwordExtra: String? = null
    private var tokenExtra: String? = null
    private val viewModel: VerifyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sourceExtra = intent.getStringExtra(EXTRA_SOURCE)!!
        emailExtra = intent.getStringExtra(EXTRA_EMAIL)!!
        nameExtra = intent.getStringExtra(EXTRA_NAME)
        passwordExtra = intent.getStringExtra(EXTRA_PASSWORD)
        tokenExtra = intent.getStringExtra(EXTRA_TOKEN)

        inputWidgets = listOf(binding.edtOtp1, binding.edtOtp2, binding.edtOtp3, binding.edtOtp4)
        actionWidgets = listOf(binding.btnLogin, binding.btnVerify)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        errorWidgets = listOf(binding.txtErrorOTP)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_verify
        isDismissed = true

        val verifySend = resources.getString(R.string.verify_send) + " "
        val spanVerifySend = SpannableString(verifySend + emailExtra)
        spanVerifySend.apply {
            setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@VerifyActivity,
                        R.color.neutral_black_30
                    )
                ),
                0,
                verifySend.length,
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

        resendCodeTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val resendCode = "${millisUntilFinished / 1000}s"
                binding.txtCountDown.text = resendCode
                binding.btnResendCode.isEnabled = false
                binding.txtResend.setTextColor(
                    ContextCompat.getColor(
                        this@VerifyActivity,
                        R.color.neutral_black_50
                    )
                )
            }

            override fun onFinish() {
                binding.txtCountDown.text = null
                binding.btnResendCode.isEnabled = true
                binding.txtResend.setTextColor(
                    ContextCompat.getColor(
                        this@VerifyActivity,
                        R.color.neutral_blue
                    )
                )
            }
        }

        inputEachField()
        setupClickListener()
    }

    private fun inputEachField() {
        inputWidgets.forEach {
            val itIndex = inputWidgets.indexOf(it)
            it.keyListener = DigitsKeyListener.getInstance("0123456789")
            it.doAfterTextChanged { s ->
                if (itIndex < inputWidgets.size - 1) {
                    if (s?.toString()?.length == 1) {
                        inputWidgets[itIndex + 1].requestFocus()
                    }
                }
            }
            inputWidgets[itIndex].setOnKeyListener { _, i, event ->
                if (itIndex > 0) {
                    if (event!!.action == KeyEvent.ACTION_DOWN
                        && i == KeyEvent.KEYCODE_DEL
                        && inputWidgets[itIndex].text.isEmpty()
                    ) {
                        inputWidgets[itIndex - 1].text = null
                        inputWidgets[itIndex - 1].requestFocus()
                    }
                }
                false
            }
        }
    }

    private fun setupClickListener() {
        binding.btnVerify.setOnClickListener {
            setFieldError(false)
            otpResult =
                binding.edtOtp1.text.toString() + binding.edtOtp2.text.toString() + binding.edtOtp3.text.toString() + binding.edtOtp4.text.toString()

            if (isFieldEmpty()) {
                setFieldError(true, eachField = true)

            } else {
                lifecycleScope.launch {
                    when (sourceExtra) {
                        EXTRA_REGISTER -> viewModel.userRegisterVerify(otpResult, emailExtra)
                        EXTRA_FORGOT -> viewModel.userForgotVerify(otpResult, emailExtra)
                    }
                }
            }
        }

        binding.btnResendCode.setOnClickListener {
            lifecycleScope.launch {
                when (sourceExtra) {
                    EXTRA_REGISTER -> {
                        when {
                            passwordExtra != null -> viewModel.userRegisterResendCode(
                                nameExtra!!,
                                emailExtra,
                                passwordExtra!!
                            )
                            tokenExtra != null -> viewModel.userRegisterGoogleResendCode(
                                nameExtra!!,
                                emailExtra,
                                tokenExtra!!
                            )
                        }
                    }
                    EXTRA_FORGOT -> viewModel.userForgotResendCode(emailExtra)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            start<LoginActivity>()
            finish()
        }
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
            when (sourceExtra) {
                EXTRA_REGISTER -> showBottomSheetDialog {
                    start<LoginActivity>()
                    finish()
                }
                EXTRA_FORGOT -> {
                    showBottomSheetDialog {
                        start<ResetPasswordActivity> {
                            putExtra("extra_email", emailExtra)
                            putExtra("extra_otp", otpResult)
                        }
                        finish()
                    }
                }
            }
        }
        viewModel.responseException.observe(this) { if (it != null) toast(it) }
        viewModel.resendMessage.observe(this) {
            toast(it)
            resendCodeTimer.start()
        }
    }

    override fun onResume() {
        super.onResume()
        setupClickListener()
    }
}