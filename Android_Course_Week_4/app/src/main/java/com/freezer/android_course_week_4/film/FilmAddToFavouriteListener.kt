package com.freezer.android_course_week_4.film

import com.freezer.android_course_week_4.film.Film

/*
using with FilmDetailActivity
 */
interface FilmAddToFavouriteListener {
    fun onClick(pos: Int, film : Film)
}