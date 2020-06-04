package com.freezer.android_course_week_6.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freezer.android_course_week_6.*
import com.freezer.android_course_week_6.dialog_fragment.AddFilmToMyFavouriteDialogFragment
import com.freezer.android_course_week_6.film.Film
import com.freezer.android_course_week_6.FilmDetailActivity
import com.freezer.android_course_week_6.database.FavFilmDatabase
import com.freezer.android_course_week_6.film.FilmGridAdapter
import com.freezer.android_course_week_6.film.FilmListAdapter
import com.freezer.android_course_week_6.film.FilmListener
import com.freezer.android_course_week_6.tmdb.TMDBService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_film_list.*
import kotlinx.coroutines.*

class TopRatingFilmListFragment : Fragment() {

    lateinit var filmListAdapter : FilmListAdapter
    lateinit var filmGridAdapter: FilmGridAdapter

    lateinit var films : ArrayList<Film>
    lateinit var favFilmDatabase : FavFilmDatabase

    val linearLayoutManager = LinearLayoutManager(activity)
    val gridLayoutManager = GridLayoutManager(activity, 2)

    private var layoutFlag: Boolean = false // True if grid

    override fun onAttach(context: Context) {
        super.onAttach(context)

        favFilmDatabase = FavFilmDatabase.getDataBase(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_film_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmListProgressBar = activity?.findViewById<ProgressBar>(R.id.film_list_progress_bar)
        filmListProgressBar?.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {
            getDataFromApi(page = 1)
            withContext(Dispatchers.Main) {
                filmListAdapter = FilmListAdapter(
                    requireContext(),
                    films,
                    listener,
                    dialogListener
                )
                filmGridAdapter = FilmGridAdapter(
                    requireContext(),
                    films,
                    listener,
                    dialogListener
                )
                filmListProgressBar?.isVisible = false
                film_list_recyclerview.layoutManager = linearLayoutManager
                film_list_recyclerview.adapter = filmListAdapter
                startLayoutAnimation()
            }
        }
        val layOutButton = requireActivity().findViewById<ImageButton>(R.id.rv_layout_btn)

        layOutButton.setOnClickListener(){
            if(!layoutFlag) {
                // Change to grid
                film_list_recyclerview.adapter = filmGridAdapter
                film_list_recyclerview.layoutManager = gridLayoutManager

                startLayoutAnimation()
                layOutButton.setImageResource(R.drawable.ic_view_list_white_24dp)
            }
            else {
                film_list_recyclerview.adapter = filmListAdapter
                film_list_recyclerview.layoutManager = linearLayoutManager

                startLayoutAnimation()
                layOutButton.setImageResource(R.drawable.ic_view_grid_white_24dp)
            }
            layoutFlag = !layoutFlag
        }

        // Implement load more feature
        var loading = true
        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        var curPage = 1

        film_list_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(layoutFlag == true)
                {
                    visibleItemCount = gridLayoutManager.childCount
                    totalItemCount = gridLayoutManager.itemCount
                    pastVisiblesItems = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                }
                else {
                    visibleItemCount = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisiblesItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                }

                if(loading) {
                    if(((visibleItemCount + pastVisiblesItems) >= totalItemCount) || (totalItemCount <= pastVisiblesItems + 2)) {
                        loading = false
                        filmListProgressBar?.isVisible = true
                        CoroutineScope(Dispatchers.IO).launch {
                            getDataFromApi(++curPage)
                            withContext(Dispatchers.Main) {
                                film_list_recyclerview.adapter?.notifyDataSetChanged()
                                loading = true
                                filmListProgressBar?.isVisible = false
                            }
                        }
                    }
                }
            }
        })
    }

    private val listener = object :
        FilmListener {
        override fun onClick(pos: Int, film: Film) {
            val intent = Intent(activity, FilmDetailActivity::class.java)
            intent.putExtra("film_object", film)
            //intent.putExtra("fav_films", favFilmsHM)
            activity?.startActivityForResult(intent, ActivityRequestCode.SEND_DATA)
        }
    }

    private val dialogListener = object :
        AddFilmToMyFavouriteDialogFragment.AddFilmToMyFavouriteDialogListener {
        override fun onPositiveButton(film : Film) {
            runBlocking {
                if(favFilmDatabase.favFilmDao().findById(film.id) != null) {
                    favFilmDatabase.favFilmDao().deleteFavFilm(film.toFavFilmEntity())
                    Toast.makeText(context, "Removed ${film.title} from My Favourite", Toast.LENGTH_SHORT).show()
                }
                else {
                    favFilmDatabase.favFilmDao().insertFavFilm(film.toFavFilmEntity())
                    Toast.makeText(context, "Added ${film.title} to My Favourite", Toast.LENGTH_SHORT).show()
                }
                film_list_recyclerview.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun startLayoutAnimation() {
        val layoutAnimationController: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        film_list_recyclerview.layoutAnimation = layoutAnimationController
        film_list_recyclerview.scheduleLayoutAnimation()
    }

    suspend fun getDataFromApi(page: Int) {
        if(page == 1) {
            films = TMDBService.getInstance().getApi().getTopRated(page = 1).results
        }
        else {
            films.addAll(TMDBService.getInstance().getApi().getTopRated(page = page).results)
        }
    }
}
