package com.freezer.android_course_week_4.film

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.freezer.android_course_week_4.FilmListener
import com.freezer.android_course_week_4.MainActivity
import com.freezer.android_course_week_4.R
import com.freezer.android_course_week_4.dialog_fragment.AddFilmToMyFavouriteDialogFragment

class FilmListAdapter(val context: Context, val data: ArrayList<Film>?, val listener: FilmListener, val dialogListener: AddFilmToMyFavouriteDialogFragment.AddFilmToMyFavouriteDialogListener) : RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder>(){
    val favFilmsHM = (context as MainActivity).favFilmsHM

    lateinit var film : Film

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film_list, parent, false)
        return FilmListViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        if (data == null)
            return 0
        return data.size
    }

    override fun onBindViewHolder(holderList: FilmListViewHolder, position: Int) {
        val film = data!![position]

        var dialogTitle =
            R.string.dialog_add_to_favourite

        if(favFilmsHM?.get(film.id) != null) {
            holderList.ivStar.setImageResource(R.drawable.ic_star_on_48dp)
            Log.d("SET_STAR_ON", film.title)
            dialogTitle =
                R.string.dialog_remove_from_favourite
        }
        else {
            holderList.ivStar.setImageResource(R.drawable.ic_star_off_48dp)
        }

        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500${film.posterPath}")
            .centerCrop()
            .apply(RequestOptions.overrideOf(180, 240))
            .into(holderList.ivPoster)

        holderList.tvTitle.text = film.title
        holderList.tvDescription.text = film.overview

        holderList.itemView.setOnClickListener {
            listener.onClick(position, film)
        }

        holderList.ivStar.setOnClickListener {
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


    class FilmListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivPoster = itemView.findViewById<ImageView>(R.id.film_image_view_list)
        var tvTitle = itemView.findViewById<TextView>(R.id.film_title_text_view_list)
        var tvDescription = itemView.findViewById<TextView>(R.id.film_overview_text_view_list)
        var ivStar = itemView.findViewById<AppCompatImageView>(R.id.film_star_btn_list)
    }
}

