package com.freezer.android_course_week_5.database

import androidx.room.*
import com.freezer.android_course_week_5.film.Film

@Dao
interface FavFilmDao {
    @Query("SELECT * FROM fav_films_table")
    suspend fun getFavFilmList() : List<FavFilmEntity>

    @Query("SELECT * FROM fav_films_table WHERE id IN(:id)")
    suspend fun findById(id: Int) : FavFilmEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavFilm(favFilmEntity: FavFilmEntity)

    @Delete()
    suspend fun deleteFavFilm(favFilmEntity: FavFilmEntity)
}