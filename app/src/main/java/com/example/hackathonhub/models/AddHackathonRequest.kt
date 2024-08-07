package com.example.hackathonhub.models

import java.util.Date

data class AddHackathonRequest(
    val creator: String, // User ID of the creator
    val location: String, // Location of the hackathon
    val startDate: Date, // Start date of the hackathon
    val endDate: Date, // End date of the hackathon
    val description: String, // Description of the hackathon
    val comments: List<Comment>, // List of comments
    val imgs: List<String>, // List of image URLs or paths
    val likes: List<String>, // List of user IDs who liked the hackathon
    val dateCreated: Date // Date when the hackathon was created
)