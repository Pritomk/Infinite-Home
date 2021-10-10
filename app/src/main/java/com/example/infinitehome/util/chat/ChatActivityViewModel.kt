package com.example.infinitehome.util.chat

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.infinitehome.dao.FirebaseChatDao
import com.example.infinitehome.dao.UserChatDao
import com.example.infinitehome.repositories.ChatRepository
import com.example.infinitehome.room.Chat
import com.example.infinitehome.room.UserChat
import com.example.infinitehome.room.UserChatDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ChatActivityViewModel(application: Application,senderUid: String) : AndroidViewModel(application) {

    private var _chats : MutableLiveData<ArrayList<Chat>> = MutableLiveData<ArrayList<Chat>>()
    val repository : ChatRepository
    private val TAG = "com.example.infinitehome.util.chat"
    val userChatDao : UserChatDao

    init {
        getIndividualChats(senderUid)
        userChatDao = UserChatDatabase.getDatabase(application).getUserChatDao()
        val firebaseChatDao = FirebaseChatDao()
        repository = ChatRepository(firebaseChatDao)
    }

    private fun getIndividualChats(senderUid : String) {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("chats")
        val myUid = Firebase.auth.currentUser!!.uid
        Log.d(TAG,"$senderUid  $myUid")
        collection
            .document(myUid)
            .collection("messages")
            .document(senderUid)
            .collection("messages")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e(TAG, "${e.message}")
                }
                if (snapshot != null) {
                    val chats = ArrayList<Chat>()
                    val documets = snapshot.documents
                    Log.d(TAG, "$documets")
                    documets.forEach {
                        val chat = it.toObject(Chat::class.java)
                        if (chat != null) {
                            chats.add(chat)
                        }
                    }
                    _chats.value = chats
                }
            }
    }


    internal var chats: MutableLiveData<ArrayList<Chat>>
        get() {return _chats}
        set(value) {_chats = value}

    fun addTextChat(uid: String, message: String) {
        repository.addTextChat(uid, message)
    }

    fun addImageChat(uid: String, imageUri: Uri) {
        repository.addImageChat(uid, imageUri)
    }
}