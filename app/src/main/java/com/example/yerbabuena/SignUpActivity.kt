package com.example.yerbabuena

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
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
            val phone: Int = (findViewById<EditText>(R.id.FieldPhoneNumber)).text.toString().toInt()
            val email: String = (findViewById<EditText>(R.id.FieldEmail)).text.toString()
            val password: String = (findViewById<EditText>(R.id.FieldPassword)).text.toString()

            /*auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveUserData(name, surname, phone, email)
                        // Sign in success, update UI with the signed-in user's information
                    } else {
                        // If sign in fails, display a message to the user.
                    }
                }*/
        }
    }

    @IgnoreExtraProperties
    data class User(val name: String? = null,
                    val surname: String? = null,
                    val phone:Int? = null,
                    val email: String? = null) {
        // Null default values create a no-argument default constructor, which is needed
        // for deserialization from a DataSnapshot.
    }

    private fun saveUserData(name:String, surname:String, phone:Int, email:String): Boolean
    {
        var usuario:User = User(name, surname, phone, email)
        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("Usuarios")
        //myRef.child("users").push().child("username").setValue("Alan")
        myRef.push().setValue(usuario)
            .addOnSuccessListener {
                // Write was successful!
                Toast.makeText(applicationContext, "ok", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                // Write failed
                Toast.makeText(applicationContext, "fail", Toast.LENGTH_SHORT).show()
            }
        return false
    }
}