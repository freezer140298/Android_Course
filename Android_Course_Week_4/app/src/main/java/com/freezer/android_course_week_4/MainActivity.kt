package com.freezer.android_course_week_4

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.freezer.android_course_week_4.film.Film
import com.freezer.android_course_week_4.fragment.MyFavouriteFilmListFragment
import com.freezer.android_course_week_4.fragment.NowPlayingFilmListFragment
import com.freezer.android_course_week_4.fragment.TopRatingFilmListFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    var favFilmsHM: HashMap<Int, Film>? = HashMap<Int, Film>()
    var favFilms: ArrayList<Film>? = ArrayList()
    lateinit var sharedPreferences: SharedPreferences

    lateinit var nowPlayingFilmListFragment: NowPlayingFilmListFragment
    lateinit var topRatingFilmListFragment: TopRatingFilmListFragment
    lateinit var myFavouriteFilmListFragment: MyFavouriteFilmListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        readFavouriteFilm()

        main_activity_toolbar.title = "Now Playing"

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.now_playing_page -> {
                    nowPlayingFilmListFragment =
                        NowPlayingFilmListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.film_list_fragment_container, nowPlayingFilmListFragment, "nowPlayingFragment")
                        .commit()
                    main_activity_toolbar.title = "Now Playing"
                    true
                }
                R.id.top_rating_page -> {
                    topRatingFilmListFragment =
                        TopRatingFilmListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.film_list_fragment_container, topRatingFilmListFragment, "topRatingFragment")
                        .commit()
                    main_activity_toolbar.title = "Top Rating"
                    true
                }
                R.id.my_favourite_page -> {
                    myFavouriteFilmListFragment =
                        MyFavouriteFilmListFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.film_list_fragment_container, myFavouriteFilmListFragment, "myFavouriteFragment")
                        .commit()
                    main_activity_toolbar.title = "My Favourite"
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            // Save Data
            val bundle = data?.extras
            val film : Film?
            val pos : Int?
            bundle?.let {
                film = it.getParcelable("fav_film")
                pos = it.getInt("film_pos")
                if (favFilmsHM?.get(film?.id) != null) {
                    favFilmsHM?.remove(film?.id)
                    favFilms?.remove(film)
                }
                else {
                    if (film != null) {
                        favFilmsHM?.put(film.id, film)
                        favFilms?.add(film)
                    }
                }
                saveFavouriteFilm()
                // Send Intent to My favourite fragment
                val intent = Intent()
                intent.putExtra("film_pos", pos)
                intent.setAction("com.freezer.android_course_week4.br")
                sendBroadcast(intent)
            }
        }
    }


    fun readFavouriteFilm() {
        val gson = Gson()
        val jsonStr = sharedPreferences.getString("MY_FAV_LIST", "")
        val filmType = object : TypeToken<ArrayList<Film>>() {}.type
        if(gson.fromJson<ArrayList<Film>>(jsonStr, filmType) != null){
            favFilms = gson.fromJson<ArrayList<Film>>(jsonStr, filmType)
        }
        for(film in favFilms.orEmpty()){
            favFilmsHM?.put(film.id, film)
        }
    }

    fun saveFavouriteFilm() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonStr = gson.toJson(favFilms)
        editor.putString("MY_FAV_LIST", jsonStr)
        editor.apply()
    }
}
