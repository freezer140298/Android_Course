package com.freezer.android_course_week_6.film

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freezer.android_course_week_6.MainActivity
import com.freezer.android_course_week_6.R
import com.freezer.android_course_week_6.database.FavFilmDatabase
import com.freezer.android_course_week_6.dialog_fragment.AddFilmToMyFavouriteDialogFragment
import kotlinx.coroutines.runBlocking

class FilmGridAdapter(val context: Context, val data: ArrayList<Film>?, val listener: FilmListener, val dialogListener: AddFilmToMyFavouriteDialogFragment.AddFilmToMyFavouriteDialogListener) : RecyclerView.Adapter<FilmGridAdapter.FilmGridViewHolder>() {
    lateinit var favFilmDatabase : FavFilmDatabase

    lateinit var film : Film

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmGridViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film_grid, parent, false)
        return FilmGridViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        if (data == null)
            return 0
        return data.size
    }

    override fun onBindViewHolder(holderGrid: FilmGridViewHolder, position: Int) {
        val film = data!![position]

        var dialogTitle =
            R.string.dialog_add_to_favourite

//        if(favFilmsHM?.get(film.id) != null) {
//            holderGrid.ivStar.setImageResource(R.drawable.ic_star_on_48dp)
//            Log.d("SET_STAR_ON", film.title)
//            dialogTitle =
//                R.string.dialog_remove_from_favourite
//        }
//        else {
//            holderGrid.ivStar.setImageResource(R.drawable.ic_star_off_48dp)
//        }

        favFilmDatabase = FavFilmDatabase.getDataBase(context)
        runBlocking {
            if(favFilmDatabase.favFilmDao().findById(film.id) != null) {
                holderGrid.ivStar.setImageResource(R.drawable.ic_star_on_48dp)
                Log.d("SET_STAR_ON", film.title)
                dialogTitle = R.string.dialog_remove_from_favourite
            }
            else {
                holderGrid.ivStar.setImageResource(R.drawable.ic_star_off_48dp)
            }
        }

        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500${film.posterPath}")
            .centerCrop()
            .into(holderGrid.ivPoster)

        holderGrid.tvTitle.text = film.title

        holderGrid.itemView.setOnClickListener {
            listener.onClick(position, film)
        }

        holderGrid.ivStar.setOnClickListener {
            val fm : FragmentManager = (context as FragmentActivity).supportFragmentManager
            val addFilmToMyFavouriteDialog =
                AddFilmToMyFavouriteDialogFragment(
                    dialogTitle,
                    dialogListener,
                    film
                )
            addFilmToMyFavouriteDialog.show(fm, null)
        }

    }

    class FilmGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivPoster = itemView.findViewById<ImageView>(R.id.film_image_view_grid)
        var tvTitle = itemView.findViewById<TextView>(R.id.film_title_text_view_grid)
        var ivStar = itemView.findViewById<ImageView>(R.id.film_star_btn_grid)
    }
}