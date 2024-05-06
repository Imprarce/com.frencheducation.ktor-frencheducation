package com.frencheducation.data.model.video

data class Video(
    val videoId: Int,
    val userId: Int,
    val rating: Int,
    val view: Int,
    val description: String,
    val title: String,
    val videoFile: String
)