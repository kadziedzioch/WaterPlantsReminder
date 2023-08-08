package com.example.watermyplants.feature_plant.data.util

import android.net.Uri
import androidx.room.TypeConverter

class UriConverter {

    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun uriFromString(string: String): Uri {
        return Uri.parse(string)
    }
}