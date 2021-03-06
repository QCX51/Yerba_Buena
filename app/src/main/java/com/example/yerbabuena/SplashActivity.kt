package com.example.yerbabuena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser == null)
        {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
        else if (!Firebase.auth.currentUser!!.isEmailVerified)
        {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
            finish()
        }
        else
        {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}