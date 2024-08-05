package com.example.yerbabuena

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

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
                "apple.com" -> {
                    // Signed In using apple id
                }
                "phone" -> {
                    // Signed In using phone auth
                }
                "password" -> {
                    if (Firebase.auth.currentUser?.isEmailVerified != true)
                    {
                        startActivity(Intent(applicationContext, SignInActivity::class.java))
                        finish()
                    }
                    else {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}