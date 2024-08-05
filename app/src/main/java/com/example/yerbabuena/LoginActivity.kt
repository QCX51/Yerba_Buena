package com.example.yerbabuena

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.yerbabuena.classes.Usuario
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var callbackManager: CallbackManager

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        handleSignInResult(result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val fButton = findViewById<Button>(R.id.Facebook)
        val gButton = findViewById<Button>(R.id.Google)
        val signin = findViewById<Button>(R.id.SignIn)
        val signup = findViewById<Button>(R.id.SignUp)

        /// Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "facebook:onCancel",
                    Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(applicationContext, "facebook:onError $error",
                    Toast.LENGTH_SHORT).show()
            }
        })

        fButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                callbackManager, //we added callback here according to new sdk 12.0.0 version of facebook
                listOf("public_profile", "email")
            )
        }
        gButton.setOnClickListener{
            signIn()
            //oneTapSignIn()
        }
        signin.setOnClickListener {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
            finish()
        }
        signup.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()
        }
    }

    private  fun updateUserInfo(userInfo: FirebaseUser?)
    {
        val ref = Firebase.database.getReference("/Usuarios").child(userInfo!!.uid)
        ref.get().addOnSuccessListener {
            var usuario = it.getValue<Usuario>()
            if (usuario == null) usuario = Usuario()
            usuario.name = userInfo.displayName
            usuario.email = userInfo.email
            usuario.phone = userInfo.phoneNumber
            usuario.thumbnail = userInfo.photoUrl.toString()
            ref.setValue(usuario)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUserInfo(task.result.user)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun handleSignInResult(data: Intent?) {
        // Result returned from launching the Sign In PendingIntent
        try {
            // Google Sign In was successful, authenticate with Firebase
            val credential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                //Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken)
            } else {
                // Shouldn't happen.
                //Log.d(TAG, "No ID token!")
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "2 Google Sign-in failed ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserInfo(task.result.user)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "3 Authentication Failed ${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn() {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(getString(R.string.web_client_id))
            .build()

        oneTapClient = Identity.getSignInClient(this)
        oneTapClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "1 Google Sign-in failed $e", Toast.LENGTH_SHORT).show()
                oneTapSignIn()
            }
    }

    private fun oneTapSignUp()
    {
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        oneTapClient = Identity.getSignInClient(this)
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    launchSignIn(result.pendingIntent)
                } catch (e: IntentSender.SendIntentException) {
                    Toast.makeText(this, "Couldn't start One Tap UI: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this) { _ ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                //Log.d(TAG, e.localizedMessage)
            }

    }

    private fun oneTapSignIn() {
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()

        // Display the One Tap UI
        oneTapClient = Identity.getSignInClient(this)
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    launchSignIn(result.pendingIntent)
                } catch (e: IntentSender.SendIntentException) {
                    Toast.makeText(this, "Couldn't start One Tap UI: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { _ ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.
                oneTapSignUp()
            }
    }

    private fun launchSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Toast.makeText(this, "Couldn't start Sign In: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Facebook sign-out
        LoginManager.getInstance().logOut()
        // Google sign-out
        oneTapClient = Identity.getSignInClient(this)
        oneTapClient.signOut().addOnCompleteListener {
            //updateUI(null)
        }
    }
}
