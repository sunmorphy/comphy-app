package com.comphy.photo.ui.custom

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.comphy.photo.R
import com.comphy.photo.databinding.CustomLoadingBinding

class CustomLoading(context: Context) : Dialog(context) {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        CustomLoadingBinding.inflate(layoutInflater)
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        setContentView(binding.root)
        setCancelable(false)
    }

    override fun show() {
        super.show()
        binding.imgLoading.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.btn_loading_anim
            )
        )
    }

    override fun dismiss() {
        binding.imgLoading.clearAnimation()
        super.dismiss()
    }

    fun showLoading(state: Boolean) {
        if (state) show() else dismiss()
    }
}