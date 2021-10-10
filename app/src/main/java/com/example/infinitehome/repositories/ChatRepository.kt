package com.example.infinitehome.repositories

import android.app.Application
import android.net.Uri
import com.example.infinitehome.dao.FirebaseChatDao

class ChatRepository(private val firebaseChatDao: FirebaseChatDao = FirebaseChatDao()) {


    fun readChats(application: Application, myUid: String) {
        firebaseChatDao.readChats(application,myUid)
    }

    fun addTextChat(uid: String, message: String) {
        firebaseChatDao.addTextChat(uid, message)
    }

    fun addImageChat(uid: String, imageUri: Uri) {
        firebaseChatDao.addImageChat(uid, imageUri)
    }
}