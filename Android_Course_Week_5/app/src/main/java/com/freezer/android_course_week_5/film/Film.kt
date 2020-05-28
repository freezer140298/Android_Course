package com.freezer.android_course_week_5.film

import android.os.Parcelable
import com.freezer.android_course_week_5.database.FavFilmEntity
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
    val genreIds: List<Int>?,
    @SerializedName("title")
    val title: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String
) : Parcelable {
    fun toFavFilmEntity() : FavFilmEntity{
        val favFilmEntity = FavFilmEntity(popularity = popularity,
                                        voteCount = voteCount,
                                        video = video,
                                        posterPath = posterPath,
                                        id = id,
                                        adult = adult,
                                        backdropPath = backdropPath,
                                        originalLanguage = originalLanguage,
                                        originalTitle = originalTitle,
                                        genreIds = genreIds,
                                        title = title,
                                        voteAverage = voteAverage,
                                        overview = overview,
                                        releaseDate = releaseDate)

        return favFilmEntity
    }

}
