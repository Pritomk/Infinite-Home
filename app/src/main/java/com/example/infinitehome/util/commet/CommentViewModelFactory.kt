package com.example.infinitehome.util.commet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.infinitehome.repositories.PostRepository

class CommentViewModelFactory(private val postRepository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CommentViewModel(postRepository) as T
    }
}