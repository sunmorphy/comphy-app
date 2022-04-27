package com.comphy.photo.ui.biodata

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.number.IntegerWidth
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseMainActivity
import com.comphy.photo.databinding.ActivityBiodataBinding
import com.comphy.photo.databinding.DialogBiodataConfirmBinding
import com.comphy.photo.ui.HomeActivity
import com.comphy.photo.ui.auth.login.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.toast.toast

@AndroidEntryPoint
class BiodataActivity : BiodataActivity() {

    private lateinit var binding: ActivityBiodataBinding
    private lateinit var name : String
    private lateinit var location : String
    private lateinit var telephone : Integer
    private lateinit var jobs: String
    private lateinit var description: String
    private lateinit var socialMedia: String
    private lateinit var sheetBiodata: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: BiodataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputWidgets = listOf(binding.edtName, binding.edtEmail, binding.edtPassword)
        errorWidgets = listOf(binding.txtErrorName, binding.txtErrorEmail, binding.txtErrorPassword)
        loadingImage = binding.imgLoadingBtn
        errorLayout = binding.errorLayout
        mainButtonText = R.string.string_create_account

        setupClickListener()
    }
    private fun setupClickListener() {
        binding.btnSave.setOnClickListener {
            setFieldError(false)
            name = binding.edtName.text.toString()
            location = binding.edtLocation.text.toString()
            telephone = binding.edtTelephone.toInteger()
            jobs = binding.edtJobs.text.toString()
            description = binding.edtDescription.text.toString()
            socialMedia = binding.edtMediaSocial.text.toString()

            if (isFieldEmpty()) {
                setFieldError(true, eachField = true)

            } else {
                lifecycleScope.launch {
                    viewModel.user(name, location, telephone, jobs, description, socialMedia)
                }
            }
        }

        binding.btn.setOnClickListener { start<Biodatactivity>() }
        binding.btnDismissError.setOnClickListener { showError(false) }
    }

        override fun setupObserver() {
            viewModel.isLoading.observe(this) { setButtonLoading(it) }
            viewModel.message.observe(this) {
                val errMessage = it.split("\n")
                binding.txtErrorTitle.text = errMessage[0]
                if (errMessage.size > 2) {
                    val error = "${errMessage[1]} ${errMessage[2]}"
                    binding.txtErrorDesc.text = error
                } else {
                    binding.txtErrorDesc.text = errMessage[1]
                }
                setFieldError(true)
            }

            viewModel.responseException.observe(this) { if (it != null) toast(it) }
            viewModel.authResponse.observe(this) {
                start<HomeActivity>()
                finish()
            }
        }
            override fun onResume() {
                super.onResume()
                setupClickListener()
            }
        }