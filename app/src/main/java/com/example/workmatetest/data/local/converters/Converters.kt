package com.example.workmatetest.data.local.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromList(value: List<Int>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toList(value: String?): List<Int>? {
        return value?.let {
            val type = object : TypeToken<List<Int>>() {}.type
            gson.fromJson(it, type)
        } ?: emptyList()
    }
}