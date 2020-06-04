package com.freezer.android_course_week_6

import com.freezer.android_course_week_6.film.Film
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
