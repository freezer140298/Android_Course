package com.freezer.android_course_week_9

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val normalBlue = findViewById<ImageView>(R.id.normal_blue)
        start_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val msDelay : Long = 300

        initFader(normal_blue)
        Handler().postDelayed({
            initFader(dart_blue)
            Handler().postDelayed({
                initFader(purple)
                Handler().postDelayed({
                    initFader(red)
                    Handler().postDelayed({
                        initFader(orange)
                        Handler().postDelayed({
                            initFader(light_blue)
                        }, msDelay)
                    }, msDelay)
                }, msDelay)
            }, msDelay)
        }, msDelay)
    }


    fun initFader(imageView: ImageView){
        val animator = ObjectAnimator.ofFloat(imageView, View.ALPHA,0f)
        animator.duration = 1400
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.interpolator = DecelerateInterpolator()
        runOnUiThread{
            animator.start()
        }
    }


}

