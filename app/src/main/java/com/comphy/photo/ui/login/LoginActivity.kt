package com.comphy.photo.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityLoginBinding
import com.comphy.photo.ui.HomeActivity
import com.comphy.photo.ui.forgot.ForgotPasswordActivity
import com.comphy.photo.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class LoginActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var statusCode = 0
    private val viewModel: LoginViewModel by viewModels()

    private val googleLogin =
        googleAuth { account ->
            println(account.serverAuthCode)
            lifecycleScope.launch {
                viewModel.userLoginGoogle(
                    account.email!!,
                    account.idToken!!
                ) {
                    viewModel.statusCode.observe(this@LoginActivity) { code ->
                        if (code == 200) {
                            start<HomeActivity>()
                        } else {
                            setGoogleError(true)
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtEmail, binding.edtPassword)
        actionWidgets = listOf(binding.btnRegister, binding.btnGoogleLogin, binding.btnLogin)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_login

        binding.btnLogin.setOnClickListener {
            setFieldError(false)
            lifecycleScope.launch {
                viewModel.userLogin(
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString()
                ) {
                    if (statusCode == 200) {
                        // TODO INTENT TO HOME
                    } else {
                        setFieldError(true)
                    }
                }
            }
        }

        binding.btnRegister.setOnClickListener { start<RegisterActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
        binding.btnForgotPassword.setOnClickListener { start<ForgotPasswordActivity>() }
        binding.btnGoogleLogin.setOnClickListener {
            setGoogleError(false)
            googleLogin.launch(
                GoogleSignIn.getClient(this, gso).signInIntent
            )
        }
    }

    override fun setupObserver() {
        viewModel.statusCode.observe(this) { statusCode = it }
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
        viewModel.message.observe(this) {
            if (statusCode != 200) {
                val errMessage = it.split("\n")
                binding.txtErrorTitle.text = errMessage[0]
                binding.txtErrorDesc.text = errMessage[1]
            }
        }
    }
}