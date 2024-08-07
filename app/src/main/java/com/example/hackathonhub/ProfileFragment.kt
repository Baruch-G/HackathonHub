package com.example.hackathonhub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hackathonhub.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImageView = view.findViewById(R.id.user_profile_img)
        usernameTextView = view.findViewById(R.id.tv_username)
        logoutButton = view.findViewById(R.id.btn_logout)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        if (isUserLoggedIn()) {
            displayUserDetails()
        } else {
            navigateToLogin()
        }

        logoutButton.setOnClickListener {
            logoutUser()
        }

        return view
    }

    private fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getString("user_token", null) != null
    }

    private fun displayUserDetails() {
        val username = sharedPreferences.getString("username", "User")
        val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

        usernameTextView.text = username
        if (!profileImageUrl.isNullOrEmpty()) {
            Picasso.get().load(profileImageUrl).into(profileImageView)
        } else {
            profileImageView.setImageResource(R.drawable.profile_icon) // Replace with your default image
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        navigateToLogin()
    }
}
