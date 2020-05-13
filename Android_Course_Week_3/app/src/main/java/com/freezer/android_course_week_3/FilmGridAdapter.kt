package com.freezer.android_course_week_3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FilmGridAdapter(val context: Context, var data: ArrayList<Film>, val listener: FilmListener) : RecyclerView.Adapter<FilmGridAdapter.FilmGridViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmGridViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_film_grid, parent, false)
        return FilmGridViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holderGrid: FilmGridViewHolder, position: Int) {
        val film = data[position]
        Glide.with(context)
            .load("https://image.tmdb.org/t/p/w500${film.posterPath}")
            .centerCrop()
            .into(holderGrid.ivPoster)

        holderGrid.tvTitle.text = film.title

        holderGrid.itemView.setOnClickListener {
            listener.onClick(position, film)
            true
        }
    }

    class FilmGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ivPoster = itemView.findViewById<ImageView>(R.id.film_image_view_grid)
        var tvTitle = itemView.findViewById<TextView>(R.id.film_title_text_view_grid)
    }
}