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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.comphy.photo.R
import splitties.toast.toast
import kotlin.properties.Delegates

abstract class BaseCommunityActivity : AppCompatActivity() {

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    protected lateinit var inputWidgets: List<EditText>
    protected lateinit var requiredWidgets: List<EditText>
    protected lateinit var actionWidgets: List<TextView>
    protected lateinit var errorWidgets: List<TextView>
    protected lateinit var responseLayout: ConstraintLayout
    protected lateinit var toolbarLoadingImage: ImageView
    protected lateinit var loadingImage: ImageView
    protected var toolbarButtonText by Delegates.notNull<Int>()
    protected var mainButtonText by Delegates.notNull<Int>()
    protected var isMainButton = false

    private val getPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            val isGranted = it.entries.all { it.value == true }
            if (!isGranted) toast("Comphy memerlukan izin anda")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupObserver()
    }

    fun requestAccessForFile(grantedAction: () -> Unit) {
        if (PERMISSIONS.all {
                ContextCompat.checkSelfPermission(
                    this, it
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
            if (isMainButton) {
                actionWidgets[actionWidgets.size - 1].text = null
                loadingImage.apply {
                    visibility = View.VISIBLE
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            this@BaseCommunityActivity,
                            R.anim.btn_loading_anim
                        )
                    )
                }
            } else {
                actionWidgets[actionWidgets.size - 2].visibility = View.INVISIBLE
                toolbarLoadingImage.apply {
                    visibility = View.VISIBLE
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            this@BaseCommunityActivity,
                            R.anim.btn_loading_anim
                        )
                    )
                }
            }
            setWidgetsEnable(false)
        } else {
            if (isMainButton) {
                actionWidgets[actionWidgets.size - 1].text = resources.getString(mainButtonText)
                loadingImage.apply {
                    clearAnimation()
                    visibility = View.GONE
                }
            } else {
                actionWidgets[actionWidgets.size - 2].text = resources.getString(mainButtonText)
                toolbarLoadingImage.apply {
                    clearAnimation()
                    visibility = View.GONE
                }
            }
            setWidgetsEnable(true)
        }
    }

    private fun setWidgetsEnable(state: Boolean) {
        inputWidgets.forEach { it.isEnabled = state }
        actionWidgets.forEach { it.isEnabled = state }
    }

    protected abstract fun init()

    protected abstract fun setupClickListener()

    protected abstract fun setupObserver()
}