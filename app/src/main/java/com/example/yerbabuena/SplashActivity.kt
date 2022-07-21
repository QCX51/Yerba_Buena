package com.example.yerbabuena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser == null)
        {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        }
        else
        {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}