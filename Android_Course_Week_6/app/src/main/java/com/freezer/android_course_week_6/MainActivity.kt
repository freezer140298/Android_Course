package com.freezer.android_course_week_6

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.freezer.android_course_week_6.database.FavFilmDatabase
import com.freezer.android_course_week_6.film.Film
import com.freezer.android_course_week_6.fragment.FragmentCommunicator
import com.freezer.android_course_week_6.fragment.MyFavouriteFilmListFragment
import com.freezer.android_course_week_6.fragment.NowPlayingFilmListFragment
import com.freezer.android_course_week_6.fragment.TopRatingFilmListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(){
    companion object{
        private val TAG = MainActivity::class.java.simpleName
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "1f9f785e18447759d43f88753cde81a8"
    }

    lateinit var favFilmDatabase : FavFilmDatabase
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        favFilmDatabase = FavFilmDatabase.getDataBase(context = this)

        when(getCurrentFragment()) {
            getString(R.string.top_rating) -> {
                bottomNavigationView.menu.findItem(R.id.top_rating_page).isChecked = true
                startTopRatingFragment()
            }
            getString(R.string.my_favourite) -> {
                bottomNavigationView.menu.findItem(R.id.my_favourite_page).isChecked = true
                startMyFavouriteFragment()
            }
           else -> {
               bottomNavigationView.menu.findItem(R.id.now_playing_page).isChecked = true
               startNowPlayingFragment()
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.now_playing_page -> {
                    startNowPlayingFragment()
                    true
                }
                R.id.top_rating_page -> {
                    startTopRatingFragment()
                    true
                }
                R.id.my_favourite_page -> {
                    startMyFavouriteFragment()
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Receive from FilmDetailActivity
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            // Send refresh data signal to Fragment
            val bundle = data?.extras
            val pos = bundle?.getInt("film_pos")
            val film = bundle?.getParcelable<Film>("fav_film")

            runBlocking {
                if (favFilmDatabase.favFilmDao().findById(film?.id!!) != null) {
                    favFilmDatabase.favFilmDao().deleteFavFilm(film.toFavFilmEntity())
                } else {
                    favFilmDatabase.favFilmDao().insertFavFilm(film.toFavFilmEntity())
                }

                val curFragment = supportFragmentManager.findFragmentById(R.id.film_list_fragment_container)

                val filmListRecyclerView = curFragment?.view?.findViewById<RecyclerView>(R.id.film_list_recyclerview)
                if(getCurrentFragment() == getString(R.string.my_favourite)) {
                    (curFragment as FragmentCommunicator).deleteFilm(film)
                    filmListRecyclerView?.adapter?.notifyItemRemoved(pos!!)
                }
                else {
                    filmListRecyclerView?.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun startNowPlayingFragment() {
        val nowPlayingFilmListFragment = NowPlayingFilmListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.film_list_fragment_container, nowPlayingFilmListFragment, "nowPlayingFragment")
            .commit()
        main_activity_toolbar.title = getString(R.string.now_playing)
        setCurrentFragment(getString(R.string.now_playing))
    }

    private fun startTopRatingFragment() {
        val topRatingFilmListFragment = TopRatingFilmListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.film_list_fragment_container, topRatingFilmListFragment, "topRatingFragment")
            .commit()
        main_activity_toolbar.title = getString(R.string.top_rating)
        setCurrentFragment(getString(R.string.top_rating))
    }

    private fun startMyFavouriteFragment() {
        val myFavouriteFilmListFragment = MyFavouriteFilmListFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.film_list_fragment_container, myFavouriteFilmListFragment, "myFavouriteFragment")
            .commit()
        main_activity_toolbar.title = getString(R.string.my_favourite)
        setCurrentFragment(getString(R.string.my_favourite))
    }

    private fun setCurrentFragment(fragment: String) {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(getString(R.string.shared_preference_current_fragment), fragment)
            commit()
        }
     }

    private fun getCurrentFragment() : String? {
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        return sharedPreferences.getString(getString(R.string.shared_preference_current_fragment), getString(R.string.now_playing))
    }
}
