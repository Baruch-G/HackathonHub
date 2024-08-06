package com.example.hackathonhub.api

import retrofit2.Call
import com.example.hackathonhub.models.Hackathon
import com.example.hackathonhub.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(
    val email: String,
    val password: String
)

interface ApiService {
    @GET("hackathon")
    suspend fun getHackathons(): List<Hackathon>


    @POST("auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>
}
