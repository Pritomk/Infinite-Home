package com.example.infinitehome.util.commet

import androidx.lifecycle.ViewModel
import com.example.infinitehome.models.Comment
import com.example.infinitehome.repositories.PostRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CommentViewModel(private val postRepository: PostRepository) : ViewModel() {
    fun addComment(postId: String, comment: Comment) {
        GlobalScope.launch {
            postRepository.addComment(postId, comment)
        }
    }
}