package com.comphy.photo.base.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.comphy.photo.ComphyApp
import com.comphy.photo.R
import com.comphy.photo.databinding.BottomSheetBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialog
import timber.log.Timber
import java.util.regex.Pattern
import kotlin.properties.Delegates

abstract class BaseAuthActivity : AppCompatActivity() {
    private lateinit var bottomSheetDialog: BottomSheetDialog

    protected lateinit var bottomSheetBinding: BottomSheetBinding
    protected lateinit var gso: GoogleSignInOptions
    protected lateinit var inputWidgets: List<EditText>
    protected lateinit var actionWidgets: List<Button>
    protected lateinit var greetingWidgets: List<TextView>
    protected lateinit var errorWidgets: List<TextView>
    protected lateinit var loadingImage: ImageView
    protected lateinit var errorLayout: ConstraintLayout
    protected var mainButtonText by Delegates.notNull<Int>()
    protected var isDismissed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetTheme)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken((application as ComphyApp).clientId())
            .requestEmail()
            .build()

        isDismissed = false

        setupObserver()
    }

    protected fun isFieldEmpty(): Boolean {
        inputWidgets.forEach {
            if (it.text.isEmpty()) {
                return true
            }
        }
        return false
    }

    protected fun googleAuth(onSuccess: (account: GoogleSignInAccount) -> Unit): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (task.isSuccessful) {
                    Timber.tag("Task Is Successful").i(account.account.toString())
                    println(account.account.toString())
                    onSuccess(account)
                } else {
                    Timber.tag("Task Is Failed").e(account.account.toString())
                    println(account.account.toString())
                    setGoogleError(true)
                }
            } catch (e: ApiException) {
                Timber.tag("Google Api Exception").e(e)
                println("Google Api Exception = $e")
                setGoogleError(true)
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
                        this@BaseAuthActivity,
                        R.anim.btn_loading_anim
                    )
                )
            }
        } else {
            actionWidgets[actionWidgets.size - 1].text = resources.getString(mainButtonText)
            inputWidgets.forEach { it.isEnabled = true }
            actionWidgets.forEach { it.isEnabled = true }
            loadingImage.apply {
                clearAnimation()
                visibility = View.GONE
            }
        }
    }

    protected fun setFieldError(state: Boolean, eachField: Boolean? = false) {
        if (state) {
            if (eachField == true) {
                inputWidgets.forEach {
                    if (it.text.isEmpty()) {
                        it.background = ContextCompat.getDrawable(this, R.drawable.widget_error)

                        if (errorWidgets.size < 2) {
                            errorWidgets.forEach { item -> item.visibility = View.VISIBLE }
                        } else {
                            errorWidgets[inputWidgets.indexOf(it)].visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                inputWidgets.forEach {
                    it.background =
                        ContextCompat.getDrawable(this, R.drawable.widget_error)
                    showError(true)
                }
            }
        } else {
            inputWidgets.forEach {
                it.background =
                    ContextCompat.getDrawable(this, R.drawable.state_field)
                showError(false)
            }
            errorWidgets.forEach { error -> error.visibility = View.GONE }
        }
    }

    protected fun setGoogleError(state: Boolean) {
        actionWidgets[actionWidgets.size - 2].background =
            if (state) {
                ContextCompat.getDrawable(this@BaseAuthActivity, R.drawable.widget_error)
            } else {
                ContextCompat.getDrawable(this@BaseAuthActivity, R.drawable.state_button)
            }
    }

    protected fun showError(state: Boolean) {
        if (state) {
            errorLayout.visibility = View.VISIBLE
            greetingWidgets.forEach {
                it.visibility = View.INVISIBLE
            }
        } else {
            errorLayout.visibility = View.GONE
            greetingWidgets.forEach {
                it.visibility = View.VISIBLE
            }
        }
    }

    protected fun showBottomSheetDialog(onFinishTask: () -> Unit) {
        val nextScreenTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val nextScreen = "Selanjutnya (${millisUntilFinished / 1000}s)"
                inputWidgets.forEach { it.isEnabled = false }
                actionWidgets.forEach { it.isEnabled = false }
                actionWidgets[actionWidgets.size - 1].apply {
                    text = nextScreen
                    isEnabled = true
                    setOnClickListener {
                        cancel()
                        onFinish()
                    }
                }
            }

            override fun onFinish() {
                actionWidgets[actionWidgets.size - 1].apply {
                    text = resources.getString(mainButtonText)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@BaseAuthActivity,
                            R.color.primary_orange
                        )
                    )
                }
                onFinishTask()
            }
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
        bottomSheetDialog.setOnDismissListener {
            if (isDismissed) {
                inputWidgets.forEach {
                    it.background =
                        ContextCompat.getDrawable(this, R.drawable.widget_focused)
                }
                actionWidgets[actionWidgets.size - 1].setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primary_green
                    )
                )
                nextScreenTimer.start()
            } else {
                onFinishTask()
            }
        }

        bottomSheetBinding.btnSheetAction.setOnClickListener {
            isDismissed = false
            bottomSheetDialog.dismiss()
        }
    }

    protected fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }

    protected fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z]).{6,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(password)

        return matcher.matches()
    }

    protected abstract fun setupObserver()

    override fun onBackPressed() {
        if (!isDismissed) super.onBackPressed()
    }
}