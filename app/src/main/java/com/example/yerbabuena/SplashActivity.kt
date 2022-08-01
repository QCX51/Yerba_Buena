package com.example.yerbabuena

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Firebase.auth.currentUser == null)
        {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            return
        }
        Firebase.auth.currentUser?.providerData?.forEach {
            when (it.providerId)
            {
                "facebook.com" -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                "google.com" -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                "password" -> {
                    if (Firebase.auth.currentUser?.isEmailVerified != true)
                    {
                        startActivity(Intent(applicationContext, SignInActivity::class.java))
                        finish()
                    }
                }
            }
        }

    }
}