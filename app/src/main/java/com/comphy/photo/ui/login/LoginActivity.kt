package com.comphy.photo.ui.login

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityLoginBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityLoginBinding

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
            lifecycleScope.launch {
                showError(false)
                setButtonLoading(true)
                delay(2000)

                if (fieldIsEmpty()) {
                    setFieldError()

                } else {
                    setFieldError()

                    if (emailValidator(binding.edtEmail.text.toString())
                        && passwordValidator(binding.edtPassword.text.toString())
                    ) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Move To Next Activity",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        showError(true)
                    }
                }
                setButtonLoading(false)
            }
        }

        binding.btnDismissError.setOnClickListener { showError(false) }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.rootView.requestFocus()
        return super.dispatchTouchEvent(ev)
    }
}