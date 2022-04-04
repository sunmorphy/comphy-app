package com.comphy.photo.ui.login

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityLoginBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        /*
         *      THIS IS AN EXAMPLE OF ERROR AND A LOADING ANIMATION
         */
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                setButtonLoading(true)
                delay(5000)
                binding.txtHello.visibility = View.INVISIBLE
                binding.txtWelcome.visibility = View.INVISIBLE
                binding.errorLayout.visibility = View.VISIBLE
                binding.edtEmail.background =
                    ContextCompat.getDrawable(this@LoginActivity, R.drawable.widget_error)

                binding.edtPassword.background =
                    ContextCompat.getDrawable(this@LoginActivity, R.drawable.widget_error)
                setButtonLoading(false)
            }
        }

        binding.btnDismissError.setOnClickListener {
            binding.txtHello.visibility = View.VISIBLE
            binding.txtWelcome.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.GONE
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        binding.rootView.requestFocus()
        return super.dispatchTouchEvent(ev)
    }

    private fun setButtonLoading(state: Boolean) {
        if (state) {
            binding.btnLogin.text = null
            binding.imgLoadingBtn.apply {
                visibility = View.VISIBLE
                startAnimation(
                    AnimationUtils.loadAnimation(
                        this@LoginActivity,
                        R.anim.btn_loading_anim
                    )
                )
            }
        } else {
            binding.imgLoadingBtn.apply {
                clearAnimation()
                visibility = View.GONE
            }
            binding.btnLogin.text = resources.getString(R.string.string_login)
        }
    }
}