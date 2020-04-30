package com.freezer.android_course_week_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_connexion.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.loginTextView

class ConnexionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connexion)

        val signUpButton = findViewById<Button>(R.id.create_an_account_button)

        signUpButton.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java).apply {

            }

            startActivity(intent)
        }

        loginTextView.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java).apply {

            }

            startActivity(intent)
        }
    }

}
