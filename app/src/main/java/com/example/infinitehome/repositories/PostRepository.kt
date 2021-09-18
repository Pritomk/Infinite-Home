package com.example.infinitehome.repositories

import android.content.Context
import android.net.Uri
import com.example.infinitehome.dao.PostDao
import com.example.infinitehome.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class PostRepository(private val postDao: PostDao) {
    suspend fun addPost(proContext: Context, imageUri: Uri, text: String) {
        postDao.addPost(proContext, imageUri, text)
    }

    suspend fun updateLikes(postId : String) {
        postDao.updateLikes(postId)
    }

}