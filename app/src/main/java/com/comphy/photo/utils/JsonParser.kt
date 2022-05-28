package com.comphy.photo.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

object JsonParser {

    fun <T> JsonObject.parseTo(cls: Class<T>): T {
        return Gson().fromJson(this, cls)
    }

    fun <T> JsonArray.parseTo(cls: Class<T>): List<T> {
        val listType = TypeToken.getParameterized(ArrayList::class.java, cls).type
        return Gson().fromJson(this, listType)
    }

    fun <T> String?.parseTo(cls: Class<T>): T {
        return Gson().fromJson(this, cls)
    }

}