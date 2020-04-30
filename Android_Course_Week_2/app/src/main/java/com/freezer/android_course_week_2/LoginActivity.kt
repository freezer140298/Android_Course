package com.freezer.android_course_week_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signUpTextView.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java).apply{

            }

            startActivity(intent)
        }
    }
}
