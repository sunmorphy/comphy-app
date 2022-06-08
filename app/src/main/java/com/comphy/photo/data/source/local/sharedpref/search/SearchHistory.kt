package com.comphy.photo.data.source.local.sharedpref.search

import android.content.Context
import com.chibatching.kotpref.KotprefModel
import java.util.*

class SearchHistory(context: Context) : KotprefModel(context) {
    val histories by stringSetPref {
        return@stringSetPref TreeSet<String>()
    }
}