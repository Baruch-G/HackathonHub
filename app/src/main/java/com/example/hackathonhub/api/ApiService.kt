package com.example.hackathonhub.api

import com.example.hackathonhub.models.AddHackathonRequest
import retrofit2.Call
import com.example.hackathonhub.models.Hackathon
import com.example.hackathonhub.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(
    val email: String,
    val password: String
)

data class LikeRequest(val userId: String)


interface ApiService {
    @GET("hackathon")
    suspend fun getHackathons(): List<Hackathon>

    @DELETE("hackathon/{id}")
    fun deleteHackathon(@Path("id") hackathonId: String): Call<Void>

    @POST("hackathon/{id}/like")
    fun likeHackathon(
        @Path("id") id: String,
        @Body request: LikeRequest
    ): Call<Void>

    @POST("auth/login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("hackathons")
    fun addHackathon(@Body hackathon: AddHackathonRequest): Call<Void>
}
