package com.comphy.photo.ui

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityResetPasswordBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResetPasswordActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtPassword, binding.edtConfirmPassword)
        actionWidgets = listOf(binding.btnBack, binding.btnRegister, binding.btnSave)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_verify

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = resources.getString(R.string.reset_success_title)
            txtSheetDesc.text = resources.getString(R.string.reset_success_description)
            btnSheetAction.text = resources.getString(R.string.string_login)
        }

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                setButtonLoading(true)
                delay(2000)

                if (fieldIsEmpty()) {
                    setFieldError()
                    Toast.makeText(this@ResetPasswordActivity, "Field Is Empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    setFieldError()
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Field Is Not Empty",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    if (passwordValidator(binding.edtPassword.text.toString())) {
                        if (binding.edtConfirmPassword.text.toString() == binding.edtPassword.text.toString()) {
                            showBottomSheetDialog {
                                Toast.makeText(
                                    this@ResetPasswordActivity,
                                    "Saved",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            binding.txtErrorTitle.text =
                                resources.getString(R.string.reset_confirm_error_title)
                            binding.txtErrorDesc.text =
                                resources.getString(R.string.reset_confirm_error_description)
                            showError(true)
                        }
                    } else {
                        binding.txtErrorTitle.text =
                            resources.getString(R.string.string_password_error_title)
                        binding.txtErrorDesc.text =
                            resources.getString(R.string.string_password_error_description)
                        showError(true)
                    }
                }
                setButtonLoading(false)
            }
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
    }
}