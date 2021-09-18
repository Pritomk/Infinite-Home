package com.example.infinitehome.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.infinitehome.repositories.PostRepository

class ExploreViewModelFactory(private val postRepository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ExploreViewModel(postRepository) as T
    }

}