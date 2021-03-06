package com.freezer.android_course_week_5.fragment

import android.content.*
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
import kotlinx.android.synthetic.main.fragment_film_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MyFavouriteFilmListFragment : Fragment(), FragmentCommunicator {
    lateinit var filmListAdapter : FilmListAdapter
    lateinit var filmGridAdapter: FilmGridAdapter

    lateinit var favFilmDatabase : FavFilmDatabase

    lateinit var favFilms : ArrayList<Film>

    lateinit var br : BroadcastReceiver

    private val linearLayoutManager = LinearLayoutManager(activity)
    private val gridLayoutManager = GridLayoutManager(activity, 2)

    private var layoutFlag: Boolean = false // True if grid

    override fun onAttach(context: Context) {
        super.onAttach(context)
        favFilmDatabase = FavFilmDatabase.getDataBase(context)
        runBlocking {
            favFilms = ArrayList(favFilmDatabase.favFilmDao().getFavFilmList().map { it.toFilm() })
        }
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
            favFilms,
            listener,
            dialogListener
        )
        filmGridAdapter = FilmGridAdapter(
            requireContext(),
            favFilms,
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
        // Listener for on item click
        override fun onClick(pos: Int, film: Film) {
            val intent = Intent(activity, FilmDetailActivity::class.java)
            intent.putExtra("film_object", film)
            intent.putExtra("film_pos", pos)
            activity?.startActivityForResult(intent, ActivityRequestCode.SEND_DATA)
        }
    }

    private val dialogListener = object :
        AddFilmToMyFavouriteDialogFragment.AddFilmToMyFavouriteDialogListener {
        override fun onPositiveButton(film : Film) {
            runBlocking {
                if(favFilmDatabase.favFilmDao().findById(film.id) != null) {
                    deleteFilm(film)
                    favFilmDatabase.favFilmDao().deleteFavFilm(film.toFavFilmEntity())
                    Toast.makeText(context, "Removed ${film.title} from My Favourite", Toast.LENGTH_SHORT).show()
                }
                else {
                    favFilms.add(film)
                    favFilmDatabase.favFilmDao().insertFavFilm(film.toFavFilmEntity())
                    Toast.makeText(context, "Added ${film.title} to My Favourite", Toast.LENGTH_SHORT).show()
                }
                film_list_recyclerview.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun deleteFilm(film: Film) {
        favFilms.remove(film)
    }
}
