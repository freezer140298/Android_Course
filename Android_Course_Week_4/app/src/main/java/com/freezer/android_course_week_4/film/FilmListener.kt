package com.freezer.android_course_week_4.film

import com.freezer.android_course_week_4.film.Film

interface FilmListener{
    fun onClick(pos: Int, film: Film)
}