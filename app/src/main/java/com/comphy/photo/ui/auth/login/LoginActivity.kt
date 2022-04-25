package com.comphy.photo.ui.auth.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseAuthActivity
import com.comphy.photo.databinding.ActivityLoginBinding
import com.comphy.photo.ui.auth.forgot.ForgotPasswordActivity
import com.comphy.photo.ui.auth.register.RegisterActivity
import com.comphy.photo.ui.biodata.BiodataActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class LoginActivity : BaseAuthActivity() {
    companion object {
        private const val EXTRA_EMAIL = "extra_email"
    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val googleLogin =
        googleAuth { account ->
            lifecycleScope.launch {
                viewModel.userLoginGoogle(
                    account.email!!,
                    account.idToken!!
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtEmail, binding.edtPassword)
        actionWidgets = listOf(binding.btnRegister, binding.btnGoogleLogin, binding.btnLogin)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        errorWidgets = listOf(binding.txtErrorEmail, binding.txtErrorPassword)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_login

        val extraEmail = intent.getStringExtra(EXTRA_EMAIL)

        if (extraEmail != null) binding.edtEmail.text = extraEmail

        setupClickListener()
    }

    private fun setupClickListener() {
        binding.btnLogin.setOnClickListener {
            setFieldError(false)
            var email = binding.edtEmail.text.toString()
            var password = binding.edtPassword.text.toString()

            if (isFieldEmpty()) {
                setFieldError(true, eachField = true)

            } else {
                when {
                    !isEmailValid(email) -> email = ""
                    !isPasswordValid(password.lowercase()) -> password = ""
                }
                lifecycleScope.launch {
                    viewModel.userLogin(email, password)
                }
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            setGoogleError(false)
            googleLogin.launch(
                GoogleSignIn.getClient(this, gso).signInIntent
            )
        }

        binding.btnRegister.setOnClickListener { start<RegisterActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
        binding.btnForgotPassword.setOnClickListener { start<ForgotPasswordActivity>() }
    }

    override fun setupObserver() {
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.message.observe(this) {
            val errMessage = it.split("\n")
            binding.txtErrorTitle.text = errMessage[0]
            binding.txtErrorDesc.text = errMessage[1]
            setFieldError(true)
        }
        viewModel.responseException.observe(this) { if (it != null) toast(it) }
        viewModel.authResponse.observe(this) {
            start<BiodataActivity>()
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        setupClickListener()
    }
}