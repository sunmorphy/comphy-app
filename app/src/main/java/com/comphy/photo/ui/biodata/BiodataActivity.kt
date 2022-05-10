package com.comphy.photo.ui.biodata

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.comphy.photo.R
import com.comphy.photo.base.activity.BaseMainActivity
import com.comphy.photo.databinding.ActivityBiodataBinding
import com.comphy.photo.databinding.DialogBiodataConfirmBinding
import com.comphy.photo.ui.main.MainActivity
import com.comphy.photo.utils.Extension
import com.comphy.photo.utils.Extension.changeDrawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class BiodataActivity : BaseMainActivity() {

    private lateinit var binding: ActivityBiodataBinding
    private lateinit var confirmDialog: Dialog
    private lateinit var dialogBiodata: DialogBiodataConfirmBinding
    private lateinit var sheetBiodata: BottomSheetBehavior<ConstraintLayout>
    private var isSuccess = false
    private val viewModel: BiodataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.getUserDetails()
            viewModel.getRegencies()
        }

        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupClickListener()
        showSuccess(false)

    }

    override fun init() {
        confirmDialog = Dialog(this)
        dialogBiodata = DialogBiodataConfirmBinding.inflate(layoutInflater)
        sheetBiodata = BottomSheetBehavior.from(binding.layoutSheetBiodata.root)

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
        viewModel.updateResponse.observe(this) {
            showSuccess(true)
            lifecycleScope.launch { delay(1500) }
            start<MainActivity>()
            finishAffinity()
        }
        viewModel.userData.observe(this) {
            if (it.location != null && it.job != null && it.description != null) {
                start<MainActivity>()
                finishAffinity()
            } else {
                binding.layoutSheetBiodata.edtName.text = it.fullname!!
            }
        }
        viewModel.regencies.observe(this) { regencies ->
            val locationAdapter =
                ArrayAdapter(
                    this@BiodataActivity,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    Extension.formatRegency(regencies)
                )

            binding.layoutSheetBiodata.edtLocation.apply {
                setDropDownBackgroundDrawable(this@BiodataActivity.changeDrawable(R.drawable.bg_dialog))
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
    }

    private fun showSuccess(state: Boolean) {
        if (state) {
            isSuccess = true
            binding.layoutSuccess.visibility = View.VISIBLE
            inputWidgets.forEach { it.isEnabled = false }
            actionWidgets.forEach { it.isEnabled = false }
        } else {
            isSuccess = false
            binding.layoutSuccess.visibility = View.GONE
            inputWidgets.forEach { it.isEnabled = true }
            actionWidgets.forEach { it.isEnabled = true }
        }
    }

    override fun onBackPressed() {
        if (!isSuccess) super.onBackPressed()
    }
}