package com.freezer.android_course_week_4.fragment

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
import com.freezer.android_course_week_4.*
import com.freezer.android_course_week_4.dialog_fragment.AddFilmToMyFavouriteDialogFragment
import com.freezer.android_course_week_4.film.Film
import com.freezer.android_course_week_4.FilmDetailActivity
import com.freezer.android_course_week_4.film.FilmGridAdapter
import com.freezer.android_course_week_4.film.FilmListAdapter
import kotlinx.android.synthetic.main.fragment_film_list.*

class MyFavouriteFilmListFragment : Fragment() {
    lateinit var filmListAdapter : FilmListAdapter
    lateinit var filmGridAdapter: FilmGridAdapter

    lateinit var favFilms : ArrayList<Film>
    lateinit var favFilmsHM : HashMap<Int, Film>

    private val linearLayoutManager = LinearLayoutManager(activity)
    private val gridLayoutManager = GridLayoutManager(activity, 2)

    private var layoutFlag: Boolean = false // True if grid

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

        val br = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                // Receive broadcast from Main Activity
                val pos = intent?.getIntExtra("film_pos", -1)
                if(pos != -1) {
                    try {
                        film_list_recyclerview.adapter?.notifyDataSetChanged()
                    } catch (e: IllegalStateException) {
                        Log.d("NULL_POINTER", e.toString())
                    }
                }
            }
        }

        context?.registerReceiver(br, object : IntentFilter("com.freezer.android_course_week4.br"){
        })
    }

    private val listener = object :
        FilmListener {
        // Listener for on item click
        override fun onClick(pos: Int, film: Film) {
            val intent = Intent(activity, FilmDetailActivity::class.java)
            intent.putExtra("film_object", film)
            intent.putExtra("fav_films", favFilmsHM)
            intent.putExtra("film_pos", pos)
            activity?.startActivityForResult(intent,
                ActivityRequestCode.SEND_DATA
            )
        }
    }

    private val dialogListener = object :
        AddFilmToMyFavouriteDialogFragment.AddFilmToMyFavouriteDialogListener {
        override fun onPositiveButton(film : Film) {
            if (favFilmsHM.get(film.id) != null) {
                favFilmsHM.remove(film.id)
                favFilms.remove(film)
                Toast.makeText(context, "Removed ${film.title} from My Favourite", Toast.LENGTH_SHORT).show()
            }
            else {
                favFilmsHM.put(film.id, film)
                favFilms.add(film)
                Toast.makeText(context, "Added ${film.title} to My Favourite", Toast.LENGTH_SHORT).show()
            }
            (context as MainActivity).saveFavouriteFilm()
            film_list_recyclerview.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(favFilms : ArrayList<Film>) =
            MyFavouriteFilmListFragment()
                .apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("fav_films", favFilms)
                }
            }
    }
}
