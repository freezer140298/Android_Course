package com.freezer.android_course_week_6

import com.freezer.android_course_week_6.film.Film
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class JsonParser{
    fun parseJson(jsonString: String): JsonResult{
        var gson = Gson()

        var result: JsonResult = gson.fromJson(jsonString, JsonResult::class.java)

        return result
    }
}