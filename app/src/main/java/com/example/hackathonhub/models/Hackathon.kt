package com.example.hackathonhub.models

data class User(
    val imgUrl: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val id: String
)

data class Comment(
    val text: String,
    val user: User,
    val id: String
)

data class Hackathon(
    val id: String,
    val creator: User,
    val location: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val comments: List<Comment>,
    val likes: List<String>,
    val img: String,
    val dateCreated: String
)