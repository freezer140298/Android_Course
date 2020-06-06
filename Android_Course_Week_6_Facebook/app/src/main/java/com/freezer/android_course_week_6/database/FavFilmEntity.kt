package com.freezer.android_course_week_6.database

import androidx.room.*
import com.freezer.android_course_week_6.film.Film

@Entity(tableName = "fav_films_table", indices = arrayOf(Index(value = ["id"])))
@TypeConverters(Converters::class)
class FavFilmEntity (
    @ColumnInfo(name ="popularity")
    var popularity: Float,
    @ColumnInfo(name ="vote_count")
    var voteCount: Int,
    @ColumnInfo(name ="video")
    var video : Boolean,
    @ColumnInfo(name ="poster_path")
    var posterPath : String? = "NULL",
    @PrimaryKey()
    var id: Int,
    @ColumnInfo(name ="adult")
    var adult: Boolean,
    @ColumnInfo(name ="backdrop_path")
    var backdropPath : String? = "NULL",
    @ColumnInfo(name ="original_language")
    var originalLanguage: String,
    @ColumnInfo(name ="original_title")
    var originalTitle: String,
    @ColumnInfo(name ="genre_ids")
    var genreIds: List<Int>?,
    @ColumnInfo(name ="title")
    var title: String,
    @ColumnInfo(name ="vote_average")
    var voteAverage: Float,
    @ColumnInfo(name ="overview")
    var overview: String,
    @ColumnInfo(name ="release_date")
    var releaseDate: String
) {
    fun toFilm() : Film{
        val film = Film(popularity = popularity,
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
        return film
    }
}