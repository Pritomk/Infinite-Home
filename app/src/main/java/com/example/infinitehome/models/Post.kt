package com.example.infinitehome.models

data class Post(
    val imageReference : String = "",
    val captionText : String = "",
    val createdBy : User = User(),
    val createdAt : Long = 0L,
    val likedBy : ArrayList<String> = ArrayList(),
    var comments : ArrayList<Comment> = ArrayList()
)
