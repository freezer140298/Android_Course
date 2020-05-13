package com.freezer.android_course_week_3

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import kotlinx.android.synthetic.main.activity_film_detail.*

class FilmDetailActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        val film: Film? = intent.getParcelableExtra("film_object")

        film_title_text_view_detail.text = film?.title
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${film?.posterPath}")
            .centerCrop()
            .into(film_image_view_detail)

        val overviewText = SpannableStringBuilder().bold { append("Overview : ") }.append(film?.overview)
        film_overview_text_view_detail.text = overviewText

        film_rating_bar_detail.rating = film?.voteAverage!! / 2
    }
}
