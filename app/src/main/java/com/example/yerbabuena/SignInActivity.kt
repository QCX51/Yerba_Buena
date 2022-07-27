package com.example.yerbabuena

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val signup: Button = findViewById(R.id.btn_signup)
        signup.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()
        }

        val email = (findViewById<EditText>(R.id.FieldEmail))
        email.setText(Firebase.auth.currentUser?.email)

        var pwdreset = findViewById<TextView>(R.id.linkForgotPassword)
        pwdreset.setOnClickListener {
            if (email.text.isNotEmpty()) {
                sendPasswordResetEmail()
            }
        }

        if (!Firebase.auth.currentUser!!.isEmailVerified) {
            sendVerificationEmail()
        }

        val signIn = findViewById<Button>(R.id.btnLogin)
        signIn.setOnClickListener {
            val email: String = (findViewById<EditText>(R.id.FieldEmail)).text.toString()
            val password: String = (findViewById<EditText>(R.id.FieldPassword)).text.toString()

            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        if (!Firebase.auth.currentUser!!.isEmailVerified) {
                            sendVerificationEmail()
                        } else {
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            applicationContext,
                            "${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun sendVerificationEmail() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.email_not_verified_title))
            .setMessage(
                getString(
                    R.string.email_not_verified_message,
                    Firebase.auth.currentUser?.email
                )
            )
        builder.setCancelable(false)

        builder.setPositiveButton("Si") { dialog, _ ->
            Firebase.auth.currentUser!!.sendEmailVerification().addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    getText(R.string.email_verification_sent),
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener { ex: Exception ->
                Toast.makeText(applicationContext, "${ex.message}", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun sendPasswordResetEmail()
    {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.email_password_reset_title))
            .setMessage(R.string.email_password_reset_message)

        builder.setCancelable(false)
        builder.setPositiveButton("Si") { dialog, _ ->
            val email = (findViewById<EditText>(R.id.FieldEmail))
            Firebase.auth.sendPasswordResetEmail(email.text.toString()).addOnSuccessListener {
                Toast.makeText(this, R.string.email_password_reset_sent, Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}