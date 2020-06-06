package com.freezer.android_course_week_6

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import com.bumptech.glide.Glide
import com.freezer.android_course_week_6.database.FavFilmDatabase
import com.freezer.android_course_week_6.film.Film
import com.freezer.android_course_week_6.film.FilmAddToFavouriteListener

import kotlinx.android.synthetic.main.activity_film_detail.*
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap

class FilmDetailActivity : AppCompatActivity(){
    var isAdded = false
    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film_detail)

        val film: Film? = intent.getParcelableExtra("film_object")
        val pos = intent.getIntExtra("film_pos", -1)

        val favFilmDatabase = FavFilmDatabase.getDataBase(this)

        film_detail_activity_toolbar.title = film?.title

        film_detail_original_title.append(" " + film?.originalTitle)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${film?.posterPath}")
            .centerCrop()
            .into(film_detail_poster)

        val overviewText = SpannableStringBuilder().bold { append("Overview : \n") }.append(film?.overview)
        film_detail_overview.text = overviewText

        film_detail_rating_bar.rating = film?.voteAverage!! / 2

        film_detail_vote_count.text = "(${film?.voteCount} votes)"
        film_detail_popularity.text = "(Popularity : ${film?.popularity})"

        val locale : Locale = Locale(film.originalLanguage)
        film_detail_language.append(" " + locale.getDisplayLanguage(Locale.ENGLISH))

        film_detail_release_date.append("\n" + film.releaseDate)

        add_to_fav_fab.setOnClickListener {
            listener.onClick(pos, film)
        }

        runBlocking {
            if(favFilmDatabase.favFilmDao().findById(film.id) != null) {
                add_to_fav_fab.imageTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.colorStarOn
                )
                isAdded = true
            }
        }
    }

    private val listener = object:
        FilmAddToFavouriteListener {
        override fun onClick(pos: Int, film: Film) {
            // Pass data back to MainActivity
            val intent: Intent = Intent()
            intent.putExtra("fav_film", film)
            intent.putExtra("film_pos", pos)
            setResult(Activity.RESULT_OK, intent)
            if(isAdded) {
                add_to_fav_fab.imageTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.colorStarOff
                )
                Toast.makeText(applicationContext,"Removed ${film.title} to My Favourite", Toast.LENGTH_SHORT).show()
            } else {
                add_to_fav_fab.imageTintList = ContextCompat.getColorStateList(applicationContext,
                    R.color.colorStarOn
                )
                Toast.makeText(applicationContext,"Added ${film.title} to My Favourite", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

