package com.comphy.photo.ui.verify

import android.os.Bundle
import android.text.method.DigitsKeyListener
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.BaseAuthActivity
import com.comphy.photo.databinding.ActivityVerifyBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class VerifyActivity : BaseAuthActivity() {
    private lateinit var binding: ActivityVerifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtOtp1, binding.edtOtp2, binding.edtOtp3, binding.edtOtp4)
        actionWidgets = listOf(binding.btnLogin, binding.btnVerify)
        greetingWidgets = listOf(binding.txtHello, binding.txtWelcome)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_verify

        bottomSheetBinding.apply {
            animView.setAnimation(R.raw.anim_success)
            txtSheetTitle.text = resources.getString(R.string.verify_success_title)
            txtSheetDesc.text = resources.getString(R.string.verify_success_description)
            btnSheetAction.text = resources.getString(R.string.verify_to_profile)
        }

        inputEachField()

        binding.btnVerify.setOnClickListener {
            inputWidgets.forEach { it.clearFocus() }
            lifecycleScope.launch {
                setButtonLoading(true)
                delay(4000)
                setButtonLoading(false)

                if (fieldIsEmpty()) {
                    setFieldError()
                    Toast.makeText(this@VerifyActivity, "Field Is Empty", Toast.LENGTH_SHORT).show()
                } else {
                    setFieldError()
                    Toast.makeText(this@VerifyActivity, "Field Is Not Empty", Toast.LENGTH_SHORT)
                        .show()
                    showBottomSheetDialog {
                        Toast.makeText(
                            this@VerifyActivity,
                            "Verified",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun inputEachField() {
        inputWidgets.forEach {
            it.keyListener = DigitsKeyListener.getInstance("0123456789")
            it.doAfterTextChanged { s ->
                if (s?.length == 1) {
                    val itIndex = inputWidgets.indexOf(it)
                    if (itIndex < inputWidgets.size - 1) {
                        inputWidgets[itIndex + 1].requestFocus()
                    }
                }
            }
        }
    }
}