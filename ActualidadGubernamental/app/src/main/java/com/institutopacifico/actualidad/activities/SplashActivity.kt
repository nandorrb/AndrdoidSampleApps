package com.institutopacifico.actualidad.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

import com.institutopacifico.actualidad.R


class SplashActivity : Activity() {


    // Splash screen timer
    private var SPLASH_TIME_OUT: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        SPLASH_TIME_OUT = this.resources.getInteger(R.integer.splash_time)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(i)

            // close this activity
            finish()
        }, SPLASH_TIME_OUT.toLong())


    }


}
