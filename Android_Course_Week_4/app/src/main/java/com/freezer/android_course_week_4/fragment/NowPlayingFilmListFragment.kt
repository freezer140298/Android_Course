package com.freezer.android_course_week_4.fragment

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
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.freezer.android_course_week_4.*
import com.freezer.android_course_week_4.dialog_fragment.AddFilmToMyFavouriteDialogFragment
import com.freezer.android_course_week_4.film.Film
import com.freezer.android_course_week_4.FilmDetailActivity
import com.freezer.android_course_week_4.film.FilmGridAdapter
import com.freezer.android_course_week_4.film.FilmListAdapter
import com.freezer.android_course_week_4.film.FilmListener
import com.khtn.androidcamp.DataCenter
import kotlinx.android.synthetic.main.fragment_film_list.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NowPlayingFilmListFragment : Fragment() {

    lateinit var filmListAdapter : FilmListAdapter
    lateinit var filmGridAdapter: FilmGridAdapter

    lateinit var films : ArrayList<Film>
    lateinit var favFilms : ArrayList<Film>
    lateinit var favFilmsHM : HashMap<Int, Film>

    private val linearLayoutManager = LinearLayoutManager(activity)
    private val gridLayoutManager = GridLayoutManager(activity, 2)

    private var layoutFlag: Boolean = false // True if grid

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val jsonParser = JsonParser()

        val jsonResult: JsonResult = jsonParser.parseJson(jsonString = DataCenter.getNowPlayingMovieJson())

        films = jsonResult.results

        favFilmsHM = (activity as MainActivity).favFilmsHM!!
        favFilms = (activity as MainActivity).favFilms!!
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

        activity?.setTitle("Now Playing")

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

    override fun onResume() {
        super.onResume()

        film_list_recyclerview.adapter?.notifyDataSetChanged()
    }

    private val listener = object :
        FilmListener {
        override fun onClick(pos: Int, film: Film) {
            val intent = Intent(activity, FilmDetailActivity::class.java)
            intent.putExtra("film_object", film)
            intent.putExtra("fav_films", favFilmsHM)
            activity?.startActivityForResult(intent,
                ActivityRequestCode.SEND_DATA
            )
        }
    }

    val dialogListener = object :
        AddFilmToMyFavouriteDialogFragment.AddFilmToMyFavouriteDialogListener {
        override fun onPositiveButton(film : Film) {
            if (favFilmsHM.get(film.id) != null) {
                favFilms.remove(film)
                favFilmsHM.remove(film.id)
                Toast.makeText(context, "Removed ${film.title} from My Favourite", Toast.LENGTH_SHORT).show()
            }
            else {
                favFilms.add(film)
                favFilmsHM.put(film.id, film)
                Toast.makeText(context, "Added ${film.title} to My Favourite", Toast.LENGTH_SHORT).show()
            }
            (context as MainActivity).saveFavouriteFilm()
            film_list_recyclerview.adapter?.notifyDataSetChanged()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyFavouriteFilmListFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
