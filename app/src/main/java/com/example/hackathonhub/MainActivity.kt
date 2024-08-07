package com.example.hackathonhub

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> {
                    switchFragment(ProfileFragment())
                    true
                }
                R.id.nav_hackathons -> {
                    switchFragment(HackathonsFragment())
                    true
                }
                R.id.nav_add_hackathon -> {
                    switchFragment(AddHackathonFragment())
                    true
                }
                else -> false
            }
        }

        // Set the default fragment
        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.nav_hackathons
        }

        // Handle navigation if passed from LoginActivity
        val selectedTabId = intent.getIntExtra("SELECTED_TAB", -1)
        if (selectedTabId != -1) {
            bottomNavigation.selectedItemId = selectedTabId
        }
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
