package com.freezer.android_course_week_6.fragment

import com.freezer.android_course_week_6.film.Film

interface FragmentCommunicator {
    fun deleteFilm(film : Film)
}