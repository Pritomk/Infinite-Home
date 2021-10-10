package com.example.infinitehome.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.infinitehome.dao.FirebaseChatDao
import com.example.infinitehome.repositories.ChatRepository
import com.example.infinitehome.repositories.UserChatRepository
import com.example.infinitehome.room.Chat
import com.example.infinitehome.room.UserChat
import com.example.infinitehome.room.UserChatDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(application: Application) : AndroidViewModel(application) {

    private val chatRepository : ChatRepository
    private val userChatRepository: UserChatRepository
    val allChats : LiveData<List<UserChat>>

    init {
        val firebaseChatDao = FirebaseChatDao()
        chatRepository = ChatRepository(firebaseChatDao)
        val userChatDao = UserChatDatabase.getDatabase(application).getUserChatDao()
        userChatRepository = UserChatRepository(userChatDao)
        allChats = userChatRepository.allChats
    }

    fun deleteChat(userChat: UserChat) = viewModelScope.launch(Dispatchers.IO) {
        userChatRepository.delete(userChat)
    }

    fun insertChat(userChat: UserChat) = viewModelScope.launch(Dispatchers.IO) {
        userChatRepository.delete(userChat)
    }

    fun updateChat(userChat: UserChat) = viewModelScope.launch(Dispatchers.IO) {
        userChatRepository.update(userChat)
    }

    fun readChats(application: Application) {
        val user = Firebase.auth
        chatRepository.readChats(application, user.currentUser!!.uid)
    }

}