package com.example.yerbabuena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @IgnoreExtraProperties
    data class User(val name: String? = null,
                    val surname: String? = null,
                    val phone: String? = null,
                    val email: String? = null,
                    val role: String? = "Cliente") {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val loginbtn: Button = findViewById(R.id.btnLogin)
        loginbtn.setOnClickListener {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
            finish()
        }

        val register: Button = findViewById(R.id.btnRegister)
        register.setOnClickListener {
            val name: String = (findViewById<EditText>(R.id.FieldName)).text.toString()
            val surname: String = (findViewById<EditText>(R.id.FieldSurnames)).text.toString()
            val phone: String = (findViewById<EditText>(R.id.FieldPhoneNumber)).text.toString()
            val email: String = (findViewById<EditText>(R.id.FieldEmail)).text.toString()
            val password: String = (findViewById<EditText>(R.id.FieldPassword)).text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        saveUserData(name, surname, phone, email)
                        task.result.user?.sendEmailVerification()?.addOnSuccessListener{
                            //auth.signOut()
                            startActivity(Intent(applicationContext, SignInActivity::class.java))
                            finish()
                        }?.addOnFailureListener{
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(applicationContext, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveUserData(name:String, surname:String, phone:String, email:String)
    {
        var usuario = User(name, surname, phone, email)
        val database = Firebase.database
        val myRef = database.getReference("/")
        myRef.child("Usuarios").push().setValue(usuario)
            .addOnSuccessListener {
                // Write was successful!
                Toast.makeText(applicationContext, "ok", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Write failed
                Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}