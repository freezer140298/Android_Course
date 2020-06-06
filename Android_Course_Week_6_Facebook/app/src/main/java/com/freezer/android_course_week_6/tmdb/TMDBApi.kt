package com.freezer.android_course_week_6.tmdb

import com.freezer.android_course_week_6.JsonResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("page") page : Int) : JsonResult

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("page") page : Int) : JsonResult
}