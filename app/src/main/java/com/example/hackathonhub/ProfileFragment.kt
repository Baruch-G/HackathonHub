package com.example.hackathonhub

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hackathonhub.R
import com.example.hackathonhub.ui.LoginActivity
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileImageView = view.findViewById(R.id.user_profile_img)
        usernameTextView = view.findViewById(R.id.tv_username) // Make sure to add a TextView for username in your layout

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        if (isUserLoggedIn()) {
            displayUserDetails()
        } else {
            navigateToLogin()
        }

        return view
    }

    private fun isUserLoggedIn(): Boolean {
        // Check if user is logged in, e.g., by checking if a token exists
        return sharedPreferences.getString("user_token", null) != null
    }

    private fun displayUserDetails() {
        // Get user details from shared preferences or API
        val username = sharedPreferences.getString("username", "User")
        val profileImageUrl = sharedPreferences.getString("profile_image_url", "")

        usernameTextView.text = username
        if (profileImageUrl != null && profileImageUrl.isNotEmpty()) {
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
}
