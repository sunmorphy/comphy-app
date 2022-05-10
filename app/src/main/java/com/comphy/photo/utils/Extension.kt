package com.comphy.photo.utils

import android.app.Activity
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.comphy.photo.data.source.local.entity.RegencyEntity
import java.io.File

object Extension {

    val File.size get() = if (!exists()) 0.0 else length().toDouble()
    val File.sizeInKb get() = size / 1024
    val File.sizeInMb get() = sizeInKb / 1024

    fun formatLocationInput(regency: String): String = "$regency, Indonesia"

    fun formatRegency(regencies: List<RegencyEntity>): MutableList<String> {
        var listRegency = mutableListOf<String>()

        regencies.forEach { regency ->
            val regencyName = regency.regencyName.split(" ")

            if (regencyName.size > 2) {
                listRegency.add(
                    formatLocationInput(
                        "${
                            regencyName[regencyName.size - 2].lowercase()
                                .replaceFirstChar { it.titlecase() }
                        } ${
                            regencyName[regencyName.size - 1].lowercase()
                                .replaceFirstChar { it.titlecase() }
                        }"
                    )
                )
            } else {
                listRegency.add(
                    formatLocationInput(
                        regencyName[regencyName.size - 1].lowercase()
                            .replaceFirstChar { it.titlecase() }
                    )
                )
            }
        }

        listRegency = listRegency.distinct().toMutableList()

        return listRegency
    }

    fun formatErrorMessage(message: String, errorTitle: TextView, errorDesc: TextView) {
        val errMessage = message.split("\n")
        errorTitle.text = errMessage[0]
        when {
            errMessage.size > 2 -> {
                val error = "${errMessage[1]} ${errMessage[2]}"
                errorDesc.text = error
            }
            errMessage.size == 1 -> {
                errorDesc.text = errMessage[0]
            }
            else -> {
                errorDesc.text = errMessage[1]
            }
        }
    }

    fun Activity.changeColor(colorId: Int) = ContextCompat.getColor(this, colorId)

    fun Activity.changeDrawable(drawableId: Int) = ContextCompat.getDrawable(this, drawableId)
}