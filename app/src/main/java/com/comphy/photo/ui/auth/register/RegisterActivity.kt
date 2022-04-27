package com.comphy.photo.ui.auth.register

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseAuthActivity
import com.comphy.photo.databinding.ActivityRegisterBinding
import com.comphy.photo.ui.auth.login.LoginActivity
import com.comphy.photo.ui.auth.verify.VerifyActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class RegisterActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private var token: String? = null
    private val viewModel: RegisterViewModel by viewModels()

    private val googleRegister =
        googleAuth { account ->
            name = account.displayName!!
            email = account.email!!
            token = account.idToken!!
            lifecycleScope.launch {
                viewModel.userRegisterGoogle(name, email, token!!)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_send)
            txtSheetTitle.text = resources.getString(R.string.register_success_title)
            btnSheetAction.text = resources.getString(R.string.string_verify)
        }

        inputWidgets = listOf(binding.edtName, binding.edtEmail, binding.edtPassword)
        actionWidgets = listOf(binding.btnLogin, binding.btnGoogleRegister, binding.btnRegister)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        errorWidgets = listOf(binding.txtErrorName, binding.txtErrorEmail, binding.txtErrorPassword)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_create_account

        setupClickListener()
    }
    private fun setupClickListener() {
        binding.btnRegister.setOnClickListener {
            setFieldError(false)
            name = binding.edtName.text.toString()
            email = binding.edtEmail.text.toString()
            password = binding.edtPassword.text.toString()

            if (isFieldEmpty()) {
                setFieldError(true, eachField = true)

            } else {
                when {
                    !isEmailValid(email) -> email = ""
                    !isPasswordValid(password.lowercase())
                            || password.contains(" ") -> password = ""
                }
                lifecycleScope.launch {
                    viewModel.userRegister(name, email, password)
                }
            }
        }

        binding.btnGoogleRegister.setOnClickListener {
            setGoogleError(false)
            googleRegister.launch(
                GoogleSignIn.getClient(this, gso).signInIntent
            )
        }

        binding.btnLogin.setOnClickListener { start<LoginActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
    }

    override fun setupObserver() {
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.message.observe(this) {
            val errMessage = it.split("\n")
            binding.txtErrorTitle.text = errMessage[0]
            if (errMessage.size > 2) {
                val error = "${errMessage[1]} ${errMessage[2]}"
                binding.txtErrorDesc.text = error
            } else {
                binding.txtErrorDesc.text = errMessage[1]
            }
            setFieldError(true)
        }
        viewModel.responseException.observe(this) { if (it != null) toast(it) }
        viewModel.authResponse.observe(this) {
            val spanMessage = SpannableString(it)
            spanMessage.apply {
                setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            this@RegisterActivity,
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
                            this@RegisterActivity,
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
                    putExtra("extra_source", "register")
                    putExtra("extra_name", name)
                    putExtra("extra_email", email)
                    putExtra("extra_password", password)
                    putExtra("extra_token", token)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setupClickListener()
    }
}