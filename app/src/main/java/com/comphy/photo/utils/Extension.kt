package com.comphy.photo.utils

import android.app.Activity
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.comphy.photo.data.source.local.entity.CityEntity
import com.comphy.photo.vo.OrientationType
import com.comphy.photo.vo.PostType
import java.io.File

object Extension {

    val File.size get() = if (!exists()) 0.0 else length().toDouble()
    val File.sizeInKb get() = size / 1024
    val File.sizeInMb get() = sizeInKb / 1024

    fun formatLocationInput(regency: String): String = "${regency.trim()}, Indonesia"

    fun formatCity(cities: List<CityEntity>): MutableList<String> {
        val listCity = mutableListOf<String>()

        cities.forEach { city ->
            val cityName = city.city.split(" ")
            var formattedCityName = ""

            for (i in 1 until cityName.size) {
                formattedCityName += "${
                    cityName[i].lowercase().replaceFirstChar { it.titlecase() }
                } "
            }
            listCity.add(formatLocationInput(formattedCityName))
        }

        return listCity
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

    fun File.isItsWidthBiggerThanHeight(mediaType: Int): Int {
        var mediaWidth = -1
        var mediaHeight = -1
        when (mediaType) {
            PostType.IMAGE -> {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(absolutePath, options)
                mediaWidth = options.outWidth
                mediaHeight = options.outHeight
            }
            PostType.VIDEO -> {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(absolutePath)
                mediaWidth =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
                        ?.toInt() ?: 0
                mediaHeight =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
                        ?.toInt() ?: 0
                retriever.release()
            }
        }

        return when {
            mediaWidth == mediaHeight -> OrientationType.SQUARE
            mediaWidth < mediaHeight -> OrientationType.PORTRAIT
            mediaWidth > mediaHeight -> OrientationType.LANDSCAPE
            else -> -1
        }
    }

    fun Activity.changeColor(colorId: Int) = ContextCompat.getColor(this, colorId)

    fun Activity.changeDrawable(drawableId: Int) = ContextCompat.getDrawable(this, drawableId)

    fun Activity.loadAnim(animId: Int): Animation = AnimationUtils.loadAnimation(this, animId)

    fun AppCompatActivity.pagerAdapter(fragments: List<Fragment>): FragmentStateAdapter {
        return object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragments.size

            override fun createFragment(position: Int): Fragment {
                val fragment: Fragment?
                fragment = fragments[position]
                return fragment
            }

        }
    }
}