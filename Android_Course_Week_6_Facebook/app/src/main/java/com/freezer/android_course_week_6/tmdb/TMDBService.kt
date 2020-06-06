package com.freezer.android_course_week_6.tmdb

import com.freezer.android_course_week_6.MainActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TMDBService{
    private var api: TMDBApi
    init {
        api = createInstance()
    }

    companion object {
        private var mInstance: TMDBService? = null

        fun getInstance() = mInstance?: synchronized(this) {
            mInstance ?: TMDBService().also { mInstance = it }
        }

        private fun createInstance(): TMDBApi {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(AuthInterceptor()).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(TMDBApi::class.java)
        }
    }

    fun getApi() : TMDBApi = api
}