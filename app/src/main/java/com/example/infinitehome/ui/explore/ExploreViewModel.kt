package com.example.infinitehome.ui.explore

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infinitehome.repositories.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExploreViewModel(private val postRepository: PostRepository) : ViewModel() {

    fun addPost(proContext: Context, imageUri: Uri, text: String) {
        GlobalScope.launch(Dispatchers.IO) {
            postRepository.addPost(proContext, imageUri, text)
        }
    }

    fun updateLikes(postId: String) {
        GlobalScope.launch {
            postRepository.updateLikes(postId)
        }
    }

    fun getImageUri(postId: String) : Uri? {
        var uri : Uri? = null
        GlobalScope.launch {
            uri = postRepository.getImageUri(postId)
        }
        return uri
    }

}