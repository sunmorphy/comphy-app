package com.comphy.photo.ui.login

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
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

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                setButtonLoading(true)
                delay(5000)
                binding.txtHello.visibility = View.INVISIBLE
                binding.txtWelcome.visibility = View.INVISIBLE
                binding.errorLayout.visibility = View.VISIBLE
                setButtonLoading(false)
            }
        }

        binding.btnDismissError.setOnClickListener {
            binding.txtHello.visibility = View.VISIBLE
            binding.txtWelcome.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.GONE
        }

        binding.edtEmail

        setFocusWidget()
    }

    private fun setFocusWidget() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.root.setOnScrollChangeListener { _, _, _, _, _ ->
                binding.btnGoogleLogin.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                binding.btnGoogleLogin.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.neutral_black
                    )
                )
            }
        }

        binding.btnGoogleLogin.setOnClickListener {
            binding.btnGoogleLogin.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.primary_orange
                )
            )
            binding.btnGoogleLogin.background =
                ContextCompat.getDrawable(it.context, R.drawable.btn_selected)
        }
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
            binding.imgLoadingBtn.visibility = View.GONE
            binding.btnLogin.text = resources.getString(R.string.string_login)
        }
    }
}