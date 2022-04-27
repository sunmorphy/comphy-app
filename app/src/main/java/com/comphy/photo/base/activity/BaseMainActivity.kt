package com.comphy.photo.base.activity

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.comphy.photo.R

abstract class BaseMainActivity : AppCompatActivity() {

    protected lateinit var inputWidgets: List<EditText>
    protected lateinit var requiredWidgets: List<EditText>
    protected lateinit var actionWidgets: List<Button>
    protected lateinit var errorWidgets: List<TextView>
    protected lateinit var responseLayout: ConstraintLayout
    protected lateinit var loadingImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserver()
    }

    protected fun isFieldEmpty(): Boolean {
        inputWidgets.forEach { if (it.text.isEmpty()) return true }
        return false
    }

    protected fun isRequiredFieldEmpty(): Boolean {
        requiredWidgets.forEach { if (it.text.isEmpty()) return true }
        return false
    }

    protected fun setRequiredFieldError(state: Boolean, eachField: Boolean? = false) {
        if (state) {
            if (eachField == true) {
                requiredWidgets.forEach {
                    if (it.text.isEmpty()) {
                        it.background = ContextCompat.getDrawable(this, R.drawable.widget_error)

                        if (errorWidgets.size < 2) {
                            errorWidgets.forEach { item -> item.visibility = View.VISIBLE }
                        } else {
                            errorWidgets[requiredWidgets.indexOf(it)].visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                requiredWidgets.forEach {
                    it.background =
                        ContextCompat.getDrawable(this, R.drawable.widget_error)
                }
            }
        } else {
            inputWidgets.forEach {
                it.background =
                    ContextCompat.getDrawable(this, R.drawable.state_field)
            }
            errorWidgets.forEach { error -> error.visibility = View.GONE }
        }
    }

    protected fun setButtonLoading(state: Boolean) {
        if (state) {
            actionWidgets[actionWidgets.size - 1].text = null
            inputWidgets.forEach { it.isEnabled = false }
            actionWidgets.forEach { it.isEnabled = false }
            loadingImage.apply {
                visibility = View.VISIBLE
                startAnimation(
                    AnimationUtils.loadAnimation(
                        this@BaseMainActivity,
                        R.anim.btn_loading_anim
                    )
                )
            }
        } else {
//            actionWidgets[actionWidgets.size - 1].text = buttonText
            inputWidgets.forEach { it.isEnabled = true }
            actionWidgets.forEach { it.isEnabled = true }
            loadingImage.apply {
                clearAnimation()
                visibility = View.GONE
            }
        }
    }

    protected fun showError(state: Boolean) =
        if (state) responseLayout.visibility = View.VISIBLE else responseLayout.visibility = View.GONE

    protected fun formatLocationInput(regency: String): String = "$regency, Indonesia"

    protected abstract fun init()

    protected abstract fun setupClickListener()

    protected abstract fun setupObserver()
}