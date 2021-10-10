package com.example.infinitehome.util.chat

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChatActivityViewModelFactory(val application: Application, val senderUid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatActivityViewModel(application,senderUid) as T
    }
}