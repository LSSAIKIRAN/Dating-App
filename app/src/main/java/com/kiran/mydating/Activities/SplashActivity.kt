package com.kiran.mydating.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.kiran.mydating.MainActivity
import com.kiran.mydating.R
import com.kiran.mydating.auth.SignInActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val user = FirebaseAuth.getInstance().currentUser

        Handler(Looper.getMainLooper()).postDelayed({

            if (user == null)
                startActivity(Intent(this, SignInActivity::class.java))
            else
                startActivity(Intent(this, MainActivity::class.java))
            finish()

        }, 3000)
    }
}

