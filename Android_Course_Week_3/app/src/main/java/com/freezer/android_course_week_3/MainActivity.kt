package com.freezer.android_course_week_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var layoutFlag: Boolean = false // True if grid

    lateinit var filmListAdapter : FilmListAdapter
    lateinit var filmGridAdapter: FilmGridAdapter

    val linearLayoutManager = LinearLayoutManager(this)
    val gridLayoutManager = GridLayoutManager(this, 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle("Films list")

        var jsonParser: JsonParser = JsonParser()

        var jsonResult: JsonResult = jsonParser.parseJson(jsonString = DataCenter.getMovieJsonString())

        var films = jsonResult.results

        filmListAdapter = FilmListAdapter(this, films, listener)
        filmGridAdapter = FilmGridAdapter(this, films, listener)

        var recyclerView = findViewById<RecyclerView>(R.id.film_list_recycler_view)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = filmListAdapter

        rv_layout_btn.setOnClickListener(){
            if(!layoutFlag) {
                // Change to grid
                recyclerView.adapter = filmGridAdapter
                recyclerView.layoutManager = gridLayoutManager
                rv_layout_btn.setImageResource(R.drawable.ic_view_list_white_24dp)
            }
            else {
                recyclerView.adapter = filmListAdapter
                recyclerView.layoutManager = linearLayoutManager
                rv_layout_btn.setImageResource(R.drawable.ic_view_grid_white_24dp)
            }
            layoutFlag = !layoutFlag
        }

    }

    private val listener = object : FilmListener{
        override fun onClick(pos: Int, film: Film) {
            val intent = Intent(this@MainActivity, FilmDetailActivity::class.java)
            intent.putExtra("film_object", film)

            startActivity(intent)
        }
    }

}
