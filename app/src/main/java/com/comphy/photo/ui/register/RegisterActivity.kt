package com.comphy.photo.ui.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityRegisterBinding
import com.comphy.photo.ui.login.LoginActivity
import com.comphy.photo.ui.verify.VerifyActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class RegisterActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var email: String
    private val viewModel: RegisterViewModel by viewModels()

    private val googleRegister =
        googleAuth { account ->
            email = account.email!!
            lifecycleScope.launch {
                viewModel.userRegisterGoogle(
                    account.email!!,
                    account.idToken!!
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtEmail, binding.edtPassword)
        actionWidgets = listOf(binding.btnLogin, binding.btnGoogleRegister, binding.btnRegister)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_create_account

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_send)
            txtSheetTitle.text = resources.getString(R.string.register_success_title)
            btnSheetAction.text = resources.getString(R.string.string_verify)
        }

        binding.btnRegister.setOnClickListener {
            setFieldError(false)
            email = binding.edtEmail.text.toString()
            lifecycleScope.launch {
                viewModel.userRegister(
                    email,
                    binding.edtPassword.text.toString()
                )
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
        viewModel.authResponse.observe(this) {
            bottomSheetBinding.txtSheetDesc.text = it
            showBottomSheetDialog {
                start<VerifyActivity> {
                    putExtra("extra_source", "register")
                    putExtra("extra_email", email)
                }
            }
        }
    }
}