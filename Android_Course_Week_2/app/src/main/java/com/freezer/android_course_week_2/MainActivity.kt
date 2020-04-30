package com.freezer.android_course_week_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startConnexionActivity(v: View) {
        val intent = Intent(this, ConnexionActivity::class.java).apply {

        }

        startActivity(intent)
    }
}
