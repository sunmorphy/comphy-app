package com.comphy.photo.ui.register

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityRegisterBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
         *      THIS IS AN EXAMPLE OF ERROR AND A LOADING ANIMATION
         */
        binding.btnRegister.setOnClickListener {
            lifecycleScope.launch {
                setButtonLoading(true)
                delay(5000)
                binding.txtHello.visibility = View.INVISIBLE
                binding.txtWelcome.visibility = View.INVISIBLE
                binding.errorLayout.visibility = View.VISIBLE
                binding.edtEmail.background =
                    ContextCompat.getDrawable(this@RegisterActivity, R.drawable.widget_error)

                binding.edtPassword.background =
                    ContextCompat.getDrawable(this@RegisterActivity, R.drawable.widget_error)
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
            binding.btnRegister.text = null
            binding.imgLoadingBtn.apply {
                visibility = View.VISIBLE
                startAnimation(
                    AnimationUtils.loadAnimation(
                        this@RegisterActivity,
                        R.anim.btn_loading_anim
                    )
                )
            }
        } else {
            binding.imgLoadingBtn.apply {
                clearAnimation()
                visibility = View.GONE
            }
            binding.btnRegister.text = resources.getString(R.string.string_login)
        }
    }
}