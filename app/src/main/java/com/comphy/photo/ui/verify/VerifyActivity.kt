package com.comphy.photo.ui.verify

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.databinding.ActivityVerifyBinding
import com.comphy.photo.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyBinding
    private lateinit var bottomSheetBinding: BottomSheetBinding
    private lateinit var inputWidgets: List<EditText>
    private lateinit var actionWidgets: List<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater, null, false)
        bottomSheetBinding.apply {
            txtSheetTitle.text = resources.getString(R.string.verify_success_title)
            txtSheetDesc.text = resources.getString(R.string.verify_success_description)
            btnVerify.text = resources.getString(R.string.verify_to_profile)
        }

        inputWidgets = listOf(binding.edtOtp1, binding.edtOtp2, binding.edtOtp3, binding.edtOtp4)
        actionWidgets = listOf(binding.btnLogin, binding.btnVerify)

        inputEachField()

        binding.btnVerify.setOnClickListener {
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
                    showBottomSheetDialog()
                }
            }
        }
    }

    private fun fieldIsEmpty(): Boolean {
        inputWidgets.forEach { edt ->
            if (edt.text.isEmpty()) {
                return true
            }
        }
        return false
    }

    private fun inputEachField() {
        inputWidgets.forEach {
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

    private fun showBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetTheme)
        val nextScreenTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val nextScreen = "Ke Halaman Selanjutnya (${millisUntilFinished / 1000}s)"
                binding.btnVerify.text = nextScreen
            }

            override fun onFinish() {
                Toast.makeText(this@VerifyActivity, "COUNT DOWN SUCCESS", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
        bottomSheetDialog.setOnDismissListener {
            inputWidgets.forEach {
                it.background =
                    ContextCompat.getDrawable(this@VerifyActivity, R.drawable.widget_focused)
            }
            binding.btnVerify.setBackgroundColor(
                ContextCompat.getColor(
                    this@VerifyActivity,
                    R.color.primary_green
                )
            )
            nextScreenTimer.start()
        }
        bottomSheetBinding.btnVerify.setOnClickListener { bottomSheetDialog.dismiss() }
    }

    private fun setButtonLoading(state: Boolean) {
        if (state) {
            binding.btnVerify.text = null
            inputWidgets.forEach { it.isEnabled = false }
            actionWidgets.forEach { it.isEnabled = false }
            binding.imgLoadingBtn.apply {
                visibility = View.VISIBLE
                startAnimation(
                    AnimationUtils.loadAnimation(
                        this@VerifyActivity,
                        R.anim.btn_loading_anim
                    )
                )
            }
        } else {
            binding.btnVerify.text = resources.getString(R.string.string_verify)
            inputWidgets.forEach { it.isEnabled = true }
            actionWidgets.forEach { it.isEnabled = true }
            binding.imgLoadingBtn.apply {
                clearAnimation()
                visibility = View.GONE
            }
        }
    }

    private fun setFieldError() {
        inputWidgets.forEach {
            if (it.text.isEmpty()) {
                it.background =
                    ContextCompat.getDrawable(this@VerifyActivity, R.drawable.widget_error)
                showError(true)
            } else {
                it.background =
                    ContextCompat.getDrawable(this@VerifyActivity, R.drawable.state_field)
                showError(false)
            }
        }
    }

    private fun showError(state: Boolean) {
        if (state) {
            binding.txtHello.visibility = View.INVISIBLE
            binding.txtWelcome.visibility = View.INVISIBLE
            binding.errorLayout.visibility = View.VISIBLE
        } else {
            binding.txtHello.visibility = View.VISIBLE
            binding.txtWelcome.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.GONE
        }
    }
}