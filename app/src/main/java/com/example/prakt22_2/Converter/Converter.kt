package com.example.prakt22_2.Converter
import androidx.room.TypeConverter

class Converter {
    @TypeConverter
    fun fromStringArray(value: Array<String>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringArray(value: String): Array<String> {
        return value.split(",").toTypedArray()
    }
}
