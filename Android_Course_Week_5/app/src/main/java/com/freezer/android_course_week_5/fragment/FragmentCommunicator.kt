package com.freezer.android_course_week_5.fragment

import com.freezer.android_course_week_5.film.Film

interface FragmentCommunicator {
    fun deleteFilm(film : Film)
}