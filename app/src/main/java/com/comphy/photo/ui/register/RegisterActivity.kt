package com.comphy.photo.ui.register

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityRegisterBinding
import com.comphy.photo.ui.login.LoginActivity
import com.comphy.photo.ui.verify.VerifyActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class RegisterActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtEmail, binding.edtPassword)
        actionWidgets = listOf(binding.btnLogin, binding.btnGoogleRegister, binding.btnRegister)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        registerErrorTitle = binding.txtErrorTitle
        registerErrorDesc = binding.txtErrorDesc
        mainButtonText = R.string.string_create_account

        bottomSheetBinding.apply {
            val sheetDesc =
                resources.getString(R.string.register_success_description) + binding.edtEmail.text.toString()
            animView.setAnimation(R.raw.anim_send)
            txtSheetTitle.text = resources.getString(R.string.register_success_title)
            txtSheetDesc.text = sheetDesc
            btnSheetAction.text = resources.getString(R.string.string_verify)
        }

        binding.btnRegister.setOnClickListener {
            lifecycleScope.launch {
                showError(false)
                setButtonLoading(true)
                delay(2000)

                if (fieldIsEmpty()) {
                    setFieldError()

                } else if (binding.edtEmail.text.toString()
                        .isEmpty() && binding.edtPassword.text.toString().isNotEmpty()
                ) {
                    binding.txtErrorTitle.text =
                        resources.getString(R.string.string_password_error_title)
                    binding.txtErrorDesc.text =
                        resources.getString(R.string.string_password_error_description)
                    showError(true)

                } else {
                    setFieldError()

                    if (emailValidator(binding.edtEmail.text.toString())
                        && passwordValidator(binding.edtPassword.text.toString())
                    ) {
                        showBottomSheetDialog { start<VerifyActivity>() }

                    } else if (!passwordValidator(binding.edtPassword.text.toString())) {
                        binding.txtErrorTitle.text =
                            resources.getString(R.string.string_password_error_title)
                        binding.txtErrorDesc.text =
                            resources.getString(R.string.string_password_error_description)
                        showError(true)

                    } else {
                        showError(true)

                    }
                }
                setButtonLoading(false)
            }
        }

        binding.btnLogin.setOnClickListener { start<LoginActivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.rootView.requestFocus()
        return super.dispatchTouchEvent(ev)
    }
}