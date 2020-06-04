package com.freezer.android_course_week_6.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(genreIdsString: String) : List<Int>? {
        return genreIdsString.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun toString(genreIdsList: List<Int>): String {
        if(genreIdsList.isEmpty())
            return ""
        return genreIdsList.joinToString(separator = ",")
    }
}