package com.freezer.android_course_week_3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class FilmListAdapter(val context: Context, val data: ArrayList<Film>, val listener: FilmListener) : RecyclerView.Adapter<FilmListAdapter.FilmListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film_list, parent, false)
        return FilmListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holderList: FilmListViewHolder, position: Int) {
        val film = data[position]
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500${film.posterPath}")
            .centerCrop()
            .apply(RequestOptions.overrideOf(180, 240))
            .into(holderList.ivPoster)

        holderList.tvTitle.text = film.title
        holderList.tvDescription.text = film.overview

        holderList.itemView.setOnClickListener {
            listener.onClick(position, film)
            true
        }
    }

//    class FilmListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        var ivPoster = itemView.findViewById<ImageView>(R.id.film_image_view_list)
//        var tvTitle = itemView.findViewById<TextView>(R.id.film_title_text_view_list)
//        var tvDescription = itemView.findViewById<TextView>(R.id.film_overview_text_view_list)
//    }

    class FilmListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivPoster = itemView.findViewById<ImageView>(R.id.film_image_view_list)
        var tvTitle = itemView.findViewById<TextView>(R.id.film_title_text_view_list)
        var tvDescription = itemView.findViewById<TextView>(R.id.film_overview_text_view_list)
    }
}