package com.comphy.photo.base.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.comphy.photo.R
import splitties.toast.toast
import kotlin.properties.Delegates

abstract class BasePostActivity : AppCompatActivity() {

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    protected lateinit var inputWidgets: List<EditText>
    protected lateinit var requiredWidgets: List<EditText>
    protected lateinit var actionWidgets: List<TextView>
    protected lateinit var errorWidgets: List<TextView>
    protected lateinit var loadingImage: ImageView
    protected var buttonText by Delegates.notNull<Int>()

    private val getPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            val isGranted = it.entries.all { it.value == true }
            if (!isGranted) toast("Comphy memerlukan izin anda")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        setupObserver()
    }

    fun requestAccessForFile(grantedAction: () -> Unit) {
        if (PERMISSIONS.all {
                ContextCompat.checkSelfPermission(
                    this,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            getPermissionResult.launch(PERMISSIONS)

        } else {
            grantedAction()
        }
    }

    protected fun isFieldEmpty(): Boolean {
        inputWidgets.forEach { if (it.text.isEmpty()) return true }
        return false
    }

    protected fun isRequiredFieldEmpty(): Boolean {
        requiredWidgets.forEach { if (it.text.isEmpty()) return true }
        return false
    }

    protected fun setButtonLoading(state: Boolean) {
        if (state) {
            actionWidgets[actionWidgets.size - 1].visibility = View.INVISIBLE
            loadingImage.apply {
                visibility = View.VISIBLE
                startAnimation(
                    AnimationUtils.loadAnimation(
                        this@BasePostActivity,
                        R.anim.btn_loading_anim
                    )
                )
            }
            setWidgetsEnable(false)
        } else {
            actionWidgets[actionWidgets.size - 1].visibility = View.VISIBLE
            loadingImage.apply {
                clearAnimation()
                visibility = View.GONE
            }
            setWidgetsEnable(true)
        }
    }

    private fun setWidgetsEnable(state: Boolean) {
        inputWidgets.forEach { it.isEnabled = state }
        actionWidgets.forEach { it.isEnabled = state }
    }

    protected abstract fun init()

    protected abstract fun setupObserver()
}