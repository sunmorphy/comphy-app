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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import splitties.toast.toast

@AndroidEntryPoint
class BiodataActivity : BaseMainActivity() {

    private lateinit var binding: ActivityBiodataBinding
    private lateinit var confirmDialog: Dialog
    private lateinit var dialogBiodata: DialogBiodataConfirmBinding
    private lateinit var sheetBiodata: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: BiodataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        confirmDialog = Dialog(this)
        dialogBiodata = DialogBiodataConfirmBinding.inflate(layoutInflater)
        sheetBiodata = BottomSheetBehavior.from(binding.layoutSheetBiodata.root)

        lifecycleScope.launch {
//            viewModel.fetchLocation()
            viewModel.getRegencies()
        }

        confirmDialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(dialogBiodata.root)
        }

        binding.layoutSheetBiodata.btnSave.setOnClickListener { confirmDialog.show() }
        dialogBiodata.btnCancel.setOnClickListener { confirmDialog.dismiss() }
        dialogBiodata.btnSave.setOnClickListener {
            confirmDialog.dismiss()
            toast("Saved")
        }

    }

    override fun setupObserver() {
//        viewModel.isFetching.observe(this) {
//            if (it) {
//                binding.layoutSheetBiodata.btnSave.isEnabled = false
//                binding.layoutSheetBiodata.edtLocation.isEnabled = false
//            } else {
//                binding.layoutSheetBiodata.btnSave.isEnabled = true
//                binding.layoutSheetBiodata.edtLocation.isEnabled = true
//            }
//        }
        viewModel.regencies.observe(this) { regencies ->
            var listRegency = mutableListOf<String>()

            regencies.forEach { regency ->
                val regencyName = regency.regencyName.split(" ")

                if (regencyName.size > 2) {
                    listRegency.add(
                        "${
                            regencyName[regencyName.size - 2].lowercase()
                                .replaceFirstChar { it.titlecase() }
                        } ${
                            regencyName[regencyName.size - 1].lowercase()
                                .replaceFirstChar { it.titlecase() }
                        }, Indonesia"
                    )
                } else {
                    listRegency.add(
                        "${
                            regencyName[regencyName.size - 1].lowercase()
                                .replaceFirstChar { it.titlecase() }
                        }, Indonesia"
                    )
                }
            }

            listRegency = listRegency.distinct().toMutableList()

            val locationAdapter =
                ArrayAdapter(
                    this@BiodataActivity,
                    R.layout.custom_list_item,
                    R.id.txtCustomItem,
                    listRegency
                )

            binding.layoutSheetBiodata.edtLocation.apply {
                setDropDownBackgroundDrawable(
//            ColorDrawable(Color.GRAY)
                    ContextCompat.getDrawable(
                        this@BiodataActivity,
                        R.drawable.bg_dialog
                    )
//                    ResourcesCompat.getDrawable(resources, R.drawable.bg_dialog, null)
//                    ContextCompat.getColor(
//                        this@BiodataActivity,
//                        splitties.material.colors.R.color.amber_600
//                    )
                )
                threshold = 1
                setAdapter(locationAdapter)
            }
        }
    }

}