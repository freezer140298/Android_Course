package com.freezer.android_course_week_5.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.freezer.android_course_week_5.*
import com.freezer.android_course_week_5.dialog_fragment.AddFilmToMyFavouriteDialogFragment
import com.freezer.android_course_week_5.film.Film
import com.freezer.android_course_week_5.FilmDetailActivity
import com.freezer.android_course_week_5.database.FavFilmDatabase
import com.freezer.android_course_week_5.film.FilmGridAdapter
import com.freezer.android_course_week_5.film.FilmListAdapter
import com.freezer.android_course_week_5.film.FilmListener
import com.khtn.androidcamp.DataCenter
import kotlinx.android.synthetic.main.fragment_film_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TopRatingFilmListFragment : Fragment() {

    lateinit var filmListAdapter : FilmListAdapter
    lateinit var filmGridAdapter: FilmGridAdapter

    lateinit var films : ArrayList<Film>
    lateinit var favFilmDatabase : FavFilmDatabase

    lateinit var br : BroadcastReceiver

    val linearLayoutManager = LinearLayoutManager(activity)
    val gridLayoutManager = GridLayoutManager(activity, 2)

    private var layoutFlag: Boolean = false // True if grid

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val jsonParser = JsonParser()

        val jsonResult: JsonResult = jsonParser.parseJson(jsonString = DataCenter.getTopRateMovieJson())

        films = jsonResult.results

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

        film_list_recyclerview.layoutManager = linearLayoutManager
        film_list_recyclerview.adapter = filmListAdapter

        val layOutButton = requireActivity().findViewById<ImageButton>(R.id.rv_layout_btn)

        fun startLayoutAnimation() {
            val layoutAnimationController: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(requireContext(),
                    R.anim.layout_animation_fall_down
                )

            film_list_recyclerview.layoutAnimation = layoutAnimationController
            film_list_recyclerview.scheduleLayoutAnimation()
        }

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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FilmListFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TopRatingFilmListFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
