package com.example.mrfarmergrocer.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.example.mrfarmergrocer.ui.activities.MainActivity
import com.example.mrfarmergrocer.R

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )


        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                // Launch the Main Activity
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish() // Call this when your activity is done and should be closed.
            },
            2500
        ) // Here we pass the delay time in milliSeconds after which the splash activity will disappear.


    }
}