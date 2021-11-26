package com.example.infinitehome.util.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StatusActivityViewModelFactory(private val uid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatusActivityViewModel(uid) as T
    }
}