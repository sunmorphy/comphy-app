package com.comphy.photo.helpers

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException

class CustomUploadTypeAdapter : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T> {
        val delegate = gson!!.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter?, value: T) {
//                out.
//                out?.beginObject()?.name("photo").value(value).endObject()
            }

            @Throws(IOException::class)
            override fun read(`in`: JsonReader?): T {
                return delegate.read(`in`)
            }
        }
    }
}