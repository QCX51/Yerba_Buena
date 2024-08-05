package com.example.yerbabuena

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.yerbabuena.classes.Ubicacion
import com.example.yerbabuena.classes.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnRegister: Button

    override fun onBackPressed()
    {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val name = (findViewById<EditText>(R.id.FieldName))
        val surname = (findViewById<EditText>(R.id.FieldSurnames))
        val phone = (findViewById<EditText>(R.id.FieldPhoneNumber))
        val email = (findViewById<EditText>(R.id.FieldEmail))
        val password = (findViewById<EditText>(R.id.FieldPassword))
        val pwdvalid = findViewById<EditText>(R.id.FieldConfirmPassword)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val fields = arrayListOf<EditText>(name, surname, phone, email, password, pwdvalid)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
            finish()
        }

        btnRegister = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            var isFieldEmpty = false
            fields.forEach { item ->
                item.setText(item.text.trim())
                if ( item.text.isEmpty() ) {
                    item.error = getString(R.string.empty_field)
                    isFieldEmpty = true
                }
            }
            if (isFieldEmpty) return@setOnClickListener
            if (!Patterns.PHONE.matcher(phone.text).matches()) {
                phone.error = getString(R.string.invalid_phone)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                email.error = getString(R.string.invalid_email)
            } else if (!password.text.isValidPassword()) {
                password.error = getString(R.string.invalid_password)
            } else if (!password.text.contains(pwdvalid.text, false) ||
                !pwdvalid.text.contains(password.text, false)) {
                Toast.makeText(this, R.string.passwords_not_match, Toast.LENGTH_SHORT).show()
            } else {
                btnRegister.isEnabled = false
                signUp(name.text.toString() + " ${surname.text.toString()}",
                    phone.text.toString(),
                    email.text.toString(),
                    password.text.toString())
            }
        }
    }

    private fun signUp(name: String,
                       phone: String,
                       email: String,
                       password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    task.result.user?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, R.string.email_verification_sent, Toast.LENGTH_LONG).show()
                    }?.addOnFailureListener {
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                        //photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                    }

                    task.result.user?.updateProfile(profileUpdates)
                    saveUserData(name, phone, email, task.result.user?.photoUrl.toString())
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    btnRegister.isEnabled = true
                }
            }
    }

    private fun saveUserData(
        name: String,
        phone: String,
        email: String,
        thumbnail: String
    ) {
        val ubicacion = Ubicacion(0.0, 0.0)
        val usuario = Usuario(name, phone, email, ubicacion)
        val myRef = Firebase.database.getReference("/")
        myRef.child("Usuarios").push().setValue(usuario)
            .addOnFailureListener {
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun CharSequence.isValidPassword(): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        val pattern = Pattern.compile(passwordPattern)
        val matcher = pattern.matcher(this)
        return matcher.matches()
    }
}