package com.freezer.android_course_week_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        loginTextView.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java).apply {

            }

            startActivity(intent)
        }
    }
}
