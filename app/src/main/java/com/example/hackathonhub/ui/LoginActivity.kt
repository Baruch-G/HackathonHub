package com.example.hackathonhub.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.hackathonhub.MainActivity
import com.example.hackathonhub.models.LoginResponse
import com.example.hackathonhub.api.ApiService
import com.example.hackathonhub.api.LoginRequest
import com.example.hackathonhub.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.hackathonhub.R

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        btnLogin.setOnClickListener {
            performLogin()
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun performLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            // Show error message
            return
        }

        val apiService = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        val call = apiService.login(LoginRequest(username, password))

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.user != null) {
                        // Save user session
                        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                        val editor = sharedPreferences.edit()
                        editor.putString("user_token", loginResponse.accessToken)
                        editor.putString("username", username)
                        editor.putString("profile_image_url", loginResponse.user.imgUrl)
                        editor.apply()

                        // Navigate to main activity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Show login failed message
                    }
                } else {
                    // Show server error message
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Show network error message
            }
        })
    }

}
