package com.comphy.photo.ui.biodata

import android.content.Context
import android.widget.ArrayAdapter

class CustomArrayAdapter(
    context: Context,
    resource: Int,
    tvResource: Int,
    objects: List<String>
) : ArrayAdapter<String>(context, resource, tvResource, objects) {
}