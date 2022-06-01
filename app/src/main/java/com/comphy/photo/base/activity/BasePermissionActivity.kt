package com.comphy.photo.base.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.anilokcun.uwmediapicker.UwMediaPicker
import com.anilokcun.uwmediapicker.model.UwMediaPickerMediaModel
import com.comphy.photo.utils.Extension.sizeInMb
import com.comphy.photo.vo.MediaSize
import splitties.toast.toast
import java.io.File

abstract class BasePermissionActivity : AppCompatActivity() {

    companion object {
        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private val getPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { it ->
            val isGranted = it.entries.all { it.value == true }
            if (!isGranted) toast("Comphy memerlukan izin anda")
        }

    fun requestAccessForFile(
        mode: UwMediaPicker.GalleryMode,
        grantedAction: (UwMediaPickerMediaModel) -> Unit
    ) {
        if (PERMISSIONS.all {
                ContextCompat.checkSelfPermission(
                    this, it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            getPermissionResult.launch(PERMISSIONS)

        } else {
            openPicker(mode) { grantedAction(it) }
        }
    }

    private fun openPicker(mode: UwMediaPicker.GalleryMode, selectedMedia: (UwMediaPickerMediaModel) -> Unit) {
        UwMediaPicker.with(this)
            .setGalleryMode(mode)
            .setGridColumnCount(2)
            .setMaxSelectableMediaCount(1)
            .setLightStatusBar(true)
            .launch {
                it?.forEach { item ->
                    val file = File(item.mediaPath)
                    if (file.sizeInMb <= MediaSize.IMAGE_NON_POST) {
                        selectedMedia(item)
                    } else {
                        toast("File harus lebih kecil dari 3MB")
                    }
                }
            }
    }

}