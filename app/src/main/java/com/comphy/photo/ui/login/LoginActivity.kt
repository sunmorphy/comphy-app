package com.comphy.photo.ui.login

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.BuildConfig
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityLoginBinding
import com.comphy.photo.ui.HomeActivity
import com.comphy.photo.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.activities.start
import timber.log.Timber


@AndroidEntryPoint
class LoginActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val googleSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            println(task)
            println(task.exception)
            println(task.isCanceled)
            println(task.isComplete)
            println(task.isSuccessful)
                val account = task.getResult(ApiException::class.java)
            println(account)
            println(account.account)
            try {
                if (account.id != null) {
                    Timber.tag("TASK IS SUCCESS").i(account.serverAuthCode)
                    lifecycleScope.launch {
                        viewModel.userGoogleLogin(
                            account.email!!,
                            account.idToken!!
                        ) { start<HomeActivity>() }
                    }
                } else {
                    Log.e("TASK IS NULL", account.id.toString())
                }
            } catch (e: ApiException) {
                Timber.tag("Google Api Exception").e(e)
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

        setupObserver()

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                showError(false)
                delay(1500)

                if (fieldIsEmpty()) {
                    setFieldError()

                } else {
                    setFieldError()

                    if (emailValidator(binding.edtEmail.text.toString())
                        && passwordValidator(binding.edtPassword.text.toString())
                    ) {
                        viewModel.userLogin(
                            binding.edtEmail.text.toString(),
                            binding.edtPassword.text.toString()
                        ) {
                            // TODO INTENT HOME ACTIVITY
                        }

                    } else {
                        showError(true)
                    }
                }
            }
        }

        binding.btnRegister.setOnClickListener { start<RegisterActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.CLIENT_ID)
            .requestEmail()
            .build()
        binding.btnGoogleLogin.setOnClickListener {
            googleSignIn.launch(
                GoogleSignIn.getClient(
                    this,
                    gso
                ).signInIntent
            )
        }
    }

    private fun setupObserver() {
        viewModel.isLoading.observe(this) { setButtonLoading(it) }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.rootView.requestFocus()
        return super.dispatchTouchEvent(ev)
    }
}