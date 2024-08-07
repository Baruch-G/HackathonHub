package com.example.hackathonhub.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathonhub.MainActivity
import com.example.hackathonhub.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createSignInIntent()
    }

    private fun createSignInIntent() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPreferences.edit()
                editor.putString("user_token", user.uid)
                editor.putString("username", user.displayName)
                editor.putString("profile_image_url", user.photoUrl?.toString())
                editor.apply()

                Log.d("Ha - ", user.photoUrl?.toString() ?: "")
                Log.d("Ha - ", user.uid.toString() ?: "")
                Log.d("Ha - ", user.displayName.toString() ?: "")

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("SELECTED_TAB", R.id.nav_hackathons) // Pass the selected tab ID
                startActivity(intent)
                finish()
            }
        } else {
            // Sign in failed
            // Handle error
        }
    }
}
