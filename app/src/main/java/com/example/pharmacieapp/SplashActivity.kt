package com.example.pharmacieapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delayed transition to MainActivity
        Handler().postDelayed({
            val intent = Intent(this, AffichageActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // Delay for 1 seconds
    }
}
