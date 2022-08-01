package com.example.yerbabuena

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val email = findViewById<EditText>(R.id.FieldEmail)
        val password = findViewById<EditText>(R.id.FieldPassword)
        email.setText(Firebase.auth.currentUser?.email)
        val fields = arrayListOf<EditText>(email, password)

        val signup: Button = findViewById(R.id.btn_signup)
        signup.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()
        }

        val pwdreset = findViewById<TextView>(R.id.linkForgotPassword)
        pwdreset.setOnClickListener {
            if (email.text.isNotEmpty()) {
                sendPasswordResetEmail()
            }
        }

        val signIn = findViewById<Button>(R.id.btnLogin)
        signIn.setOnClickListener {
            var isFieldEmpty = false
            fields.forEach { item ->
                item.setText(item.text.trim())
                if ( item.text.isEmpty() ) {
                    item.error = getString(R.string.empty_field)
                    isFieldEmpty = true
                }
            }

            if (isFieldEmpty) return@setOnClickListener

            else if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                email.error = getString(R.string.invalid_email)
            }
            else signIn(email.text.toString(), password.text.toString())
        }
    }

    private fun signIn(email: String, password: String)
    {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    if (!Firebase.auth.currentUser!!.isEmailVerified) {
                        sendVerificationEmail()
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    // Sign in fails, display a message to the user.
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerificationEmail() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.email_not_verified_title))
            .setMessage(getString(
                    R.string.email_not_verified_message,
                    Firebase.auth.currentUser?.email
                ))
        builder.setCancelable(false)

        builder.setPositiveButton("Si") { dialog, _ ->
            Firebase.auth.currentUser!!.sendEmailVerification().addOnSuccessListener {
                Toast.makeText(this, getText(R.string.email_verification_sent), Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { ex: Exception ->
                Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
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
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.email_password_reset_title))
            .setMessage(R.string.email_password_reset_message)

        builder.setCancelable(false)
        builder.setPositiveButton("Si") { dialog, _ ->
            val email = (findViewById<EditText>(R.id.FieldEmail))
            Firebase.auth.sendPasswordResetEmail(email.text.toString()).addOnSuccessListener {
                Toast.makeText(this, R.string.email_password_reset_sent, Toast.LENGTH_SHORT).show()
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