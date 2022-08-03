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
import com.example.yerbabuena.classes.Ubicacion
import com.example.yerbabuena.classes.Usuario
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.google.firebase.auth.FacebookAuthProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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

        FirebaseApp.initializeApp(/*context=*/this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
        /// Initialize Firebase Auth
        auth = Firebase.auth
        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
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
            //signIn()
            oneTapSignIn()
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

    private  fun updateUserInfo(task: Task<AuthResult>)
    {
        var ref = Firebase.database.getReference("/Usuarios")
        ref =  ref.child(task.result.user!!.uid)
        ref.get().addOnSuccessListener {
            val user = Usuario()
            user.name = task.result.user!!.displayName
            user.surname = ""
            user.email = task.result.user!!.email
            user.phone = task.result.user!!.phoneNumber
            user.location = Ubicacion(0.0, 0.0)
            val usuario = it.getValue<Usuario>()
            if (usuario == null && usuario?.role == null) {
                user.role = getString(R.string.default_role)
            }
            ref.setValue(user)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUserInfo(task)
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
                    updateUserInfo(task)
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

        oneTapClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "1 Google Sign-in failed $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun oneTapSignUp()
    {
        oneTapClient = Identity.getSignInClient(this)
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
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    launchSignIn(result.pendingIntent)
                } catch (e: IntentSender.SendIntentException) {
                    Toast.makeText(this, "Couldn't start One Tap UI: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener(this) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                //Log.d(TAG, e.localizedMessage)
            }

    }

    private fun oneTapSignIn() {
        // Configure One Tap UI
        oneTapClient = Identity.getSignInClient(this)

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
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                launchSignIn(result.pendingIntent)
            }
            .addOnFailureListener { e ->
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
