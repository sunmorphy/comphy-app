package com.comphy.photo.ui.biodata

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.comphy.photo.ui.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.activities.start

@AndroidEntryPoint
class BiodataActivity : BaseMainActivity() {

    private lateinit var binding: ActivityBiodataBinding
    private lateinit var confirmDialog: Dialog
    private lateinit var dialogBiodata: DialogBiodataConfirmBinding
    private lateinit var sheetBiodata: BottomSheetBehavior<ConstraintLayout>
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
                    binding.layoutSheetBiodata.edtLocation.text.toString(),
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
            start<MainActivity>()
            finishAffinity()
        }
        viewModel.userFullname.observe(this) { binding.layoutSheetBiodata.edtName.text = it }
        viewModel.regencies.observe(this) { regencies ->
            var listRegency = mutableListOf<String>()

            regencies.forEach { regency ->
                val regencyName = regency.regencyName.split(" ")

                if (regencyName.size > 2) {
                    val splitRegency =
                        regencyName[regencyName.size - 2].lowercase()
                            .replaceFirstChar { it.titlecase() } +
                                regencyName[regencyName.size - 2].lowercase()
                                    .replaceFirstChar { it.titlecase() }

                    listRegency.add("$splitRegency, Indonesia")

                } else {
                    val splitRegency = regencyName[regencyName.size - 1].lowercase()
                        .replaceFirstChar { it.titlecase() }

                    listRegency.add("$splitRegency, Indonesia")
                }
            }

            listRegency = listRegency.distinct().toMutableList()

            val locationAdapter =
                ArrayAdapter(
                    this@BiodataActivity,
                    R.layout.custom_dropdown_location,
                    R.id.txtLocationItem,
                    listRegency
                )

            binding.layoutSheetBiodata.edtLocation.apply {
                setDropDownBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this@BiodataActivity,
                        R.drawable.bg_dialog
                    )
                )
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
    }
}