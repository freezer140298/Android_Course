package com.freezer.android_course_week_5

import com.freezer.android_course_week_5.film.Film
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class Dates(
    @SerializedName("maximum")
    val maximum: String,
    @SerializedName("minimum")
    val minimum: String
)

data class JsonResult(
    @SerializedName("results")
    val results : ArrayList<Film>,
    @SerializedName("page")
    val page : Int,
    @SerializedName("total_results")
    val totalResults : Int,
    @SerializedName("dates")
    val dates : Dates,
    @SerializedName("total_pages")
    val totalPages : Int

)
class JsonParser{
    fun parseJson(jsonString: String): JsonResult{
        var gson = Gson()

        var result: JsonResult = gson.fromJson(jsonString, JsonResult::class.java)

        return result
    }
}