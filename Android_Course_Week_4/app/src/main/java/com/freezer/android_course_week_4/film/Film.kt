package com.freezer.android_course_week_4.film

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class Film(
    @SerializedName("popularity")
    val popularity: Float,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("video")
    val video : Boolean,
    @SerializedName("poster_path")
    val posterPath : String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath : String? = "NULL",
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("genre_ids")
    val genreIds: ArrayList<Int>?,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String
) : Parcelable