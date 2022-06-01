package com.comphy.photo.ui.biodata

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseMainActivity
import com.comphy.photo.databinding.ActivityBiodataBinding
import com.comphy.photo.databinding.DialogBiodataConfirmBinding
import com.comphy.photo.ui.custom.CustomLoading
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.utils.Extension.formatCity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.activities.start
import splitties.resources.drawable
import splitties.toast.toast

@AndroidEntryPoint
class BiodataActivity : BaseMainActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityBiodataBinding.inflate(
            layoutInflater
        )
    }
    private val confirmDialog by lazy { Dialog(this) }
    private val dialogBiodata by lazy { DialogBiodataConfirmBinding.inflate(layoutInflater) }
    private val customLoading by lazy(LazyThreadSafetyMode.NONE) { CustomLoading(this) }
    private val viewModel: BiodataViewModel by viewModels()
    private var isSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.getCities()
            viewModel.getUserDetails()
        }

        setupClickListener()
        showSuccess(false)
    }

    override fun init() {
        confirmDialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(dialogBiodata.root)
        }

        inputWidgets = listOf(
            binding.layoutSheetBiodata.edtName,
            binding.layoutSheetBiodata.edtLocation,
            binding.layoutSheetBiodata.edtNumber,
            binding.layoutSheetBiodata.edtJob,
            binding.layoutSheetBiodata.edtDescription,
            binding.layoutSheetBiodata.edtMediaSocial
        )
        requiredWidgets = listOf(
            binding.layoutSheetBiodata.edtName,
            binding.layoutSheetBiodata.edtLocation,
            binding.layoutSheetBiodata.edtJob,
            binding.layoutSheetBiodata.edtDescription
        )
        actionWidgets = listOf(binding.layoutSheetBiodata.btnSave)
        errorWidgets = listOf(
            binding.layoutSheetBiodata.txtErrorName,
            binding.layoutSheetBiodata.txtErrorLocation,
            binding.layoutSheetBiodata.txtErrorJob,
            binding.layoutSheetBiodata.txtErrorDescription
        )
        responseLayout = binding.layoutSuccess
    }

    override fun setupClickListener() {
        binding.layoutSheetBiodata.btnSave.setOnClickListener {
            setRequiredFieldError(false)

            if (isRequiredFieldEmpty()) {
                setRequiredFieldError(true, eachField = true)
            } else {
                confirmDialog.show()
            }
        }
        dialogBiodata.btnCancel.setOnClickListener { confirmDialog.dismiss() }
        dialogBiodata.btnSave.setOnClickListener {
            lifecycleScope.launch {
                viewModel.updateUserDetails(
                    binding.layoutSheetBiodata.edtName.text.toString(),
                    binding.layoutSheetBiodata.edtLocation.text.toString().split(",")[0],
                    binding.layoutSheetBiodata.edtNumber.text.toString(),
                    binding.layoutSheetBiodata.edtJob.text.toString(),
                    binding.layoutSheetBiodata.edtDescription.text.toString(),
                    binding.layoutSheetBiodata.edtMediaSocial.text.toString()
                )
            }
            confirmDialog.dismiss()
        }
    }

    override fun setupObserver() {
        viewModel.isFetching.observe(this) { setWidgetsEnable(it) }
        viewModel.exceptionResponse.observe(this) { toast(it) }
        viewModel.updateResponse.observe(this) {
            showSuccess(true)
            lifecycleScope.launch { delay(1000) }
            start<MainActivity>()
            finishAffinity()
        }
        viewModel.userData.observe(this) {
            if (it?.location != null && it.job != null && it.description != null) {
                start<MainActivity>()
                finishAffinity()
            } else {
                binding.layoutSheetBiodata.edtName.text = it?.fullname!!
            }
        }
        viewModel.cities.observe(this) {
            val locationAdapter =
                ArrayAdapter(
                    this,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    formatCity(it)
                )

            binding.layoutSheetBiodata.edtLocation.apply {
                setDropDownBackgroundDrawable(drawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
    }

    private fun showSuccess(state: Boolean) {
        if (state) {
            isSuccess = true
            binding.layoutSuccess.visibility = View.VISIBLE
            setWidgetsEnable(false)
        } else {
            isSuccess = false
            binding.layoutSuccess.visibility = View.GONE
            setWidgetsEnable(true)
        }
    }

    private fun setWidgetsEnable(state: Boolean) {
        customLoading.showLoading(state)
        inputWidgets.forEach { it.isEnabled = !state }
        actionWidgets.forEach { it.isEnabled = !state }
    }

    override fun onBackPressed() {
        if (!isSuccess) super.onBackPressed()
    }
}