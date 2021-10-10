package com.example.infinitehome.repositories

import androidx.lifecycle.LiveData
import com.example.infinitehome.dao.UserChatDao
import com.example.infinitehome.room.UserChat

class UserChatRepository(private val userChatDao: UserChatDao) {
    val allChats: LiveData<List<UserChat>> = userChatDao.getAllUserChats()

    suspend fun insert(userChat: UserChat) {
        userChatDao.insert(userChat)
    }

    suspend fun update(userChat: UserChat) {
        userChatDao.update(userChat)
    }

    suspend fun delete(userChat: UserChat) {
        userChatDao.delete(userChat)
    }

}