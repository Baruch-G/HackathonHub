package com.example.hackathonhub.models

data class LoginResponse(
    val accessToken: String,
    val user : User
)
