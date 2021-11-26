package com.example.infinitehome.ui.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StatusViewModelFactory(val uId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatusViewModel(uId) as T
    }

}