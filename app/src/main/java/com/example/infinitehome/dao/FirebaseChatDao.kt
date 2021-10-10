package com.example.infinitehome.dao

import android.app.Application
import android.net.Uri
import android.util.Log
import com.example.infinitehome.repositories.ChatRepository
import com.example.infinitehome.repositories.UserChatRepository
import com.example.infinitehome.room.Chat
import com.example.infinitehome.room.UserChat
import com.example.infinitehome.room.UserChatDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.tasks.await


class FirebaseChatDao {

    private val db = FirebaseFirestore.getInstance()
    private val chatCollection = db.collection("chats")
    private val auth = Firebase.auth
    private val TAG = "com.example.infinitehome.dao"

    fun addTextChat(uid: String, message: String) {
        GlobalScope.launch {
            val myUid = auth.currentUser!!.uid
            val timeStamp = System.currentTimeMillis()
            val chat = Chat( 0, myUid, "text", message,timeStamp)
            chatCollection.document(myUid).collection("messages").document(uid)
                .collection("messages").add(chat)
            chatCollection.document(uid).collection("messages").document(myUid)
                .collection("messages").add(chat)
        }
    }

    fun addImageChat(uid: String, imageUri: Uri) {
        GlobalScope.launch {
            val myUid = auth.currentUser!!.uid
            val timeStamp = System.currentTimeMillis()
            val imageReference = imageUpload(imageUri)

            val chat = Chat(0,myUid,"image",imageReference,timeStamp)
            chatCollection.document(myUid).collection("messages").document(uid)
                .collection("messages").add(chat)
            chatCollection.document(uid).collection("messages").document(myUid)
                .collection("messages").add(chat)
        }
    }

    @DelicateCoroutinesApi
    fun readChats(application: Application, myUid: String) {
        val userChatDao = UserChatDatabase.getDatabase(application).getUserChatDao()
        val repository = UserChatRepository(userChatDao)
        Log.d("query_receiver", "Executed")
        chatCollection
            .document(myUid)
            .collection("messages")
            .get()
            .addOnSuccessListener { task ->
                GlobalScope.launch {
                    if (!task.isEmpty) {
                        val userDao = UserDao()
                        for (document in task) {
                            val userChat = userDao.getUserId(document.id).await().toObject(UserChat::class.java)!!
                            repository.insert(userChat)
                            Log.d(TAG,document.id)
                            Log.d(TAG,"${userChat.displayName}  ${userChat.imageUrl}  ${userChat.uid}")
                        }
                    }
                }

            }
            .addOnFailureListener {
                Log.d(TAG, "message ${it.message}")
            }


    }

    private fun compareString(str1: String, str2: String): Boolean {
        val comp = str1.compareTo(str2)
        return comp < 0
    }

    private fun getTimeDate(): String {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        return "$currentDate/$currentTime"
    }

    private fun imageUpload(imageUri: Uri): String {
        val storage = Firebase.storage
        val user = Firebase.auth.currentUser!!

        val path: String = user.uid
        val referenceChild = "chat_images/$path/${getTimeDate()}"
        val reference = storage.reference.child(referenceChild)
        reference.putFile(imageUri)
            .addOnSuccessListener {
                Log.e("image_url", it.metadata?.path.toString())
            }
            .addOnFailureListener { e: Exception ->
                Log.e("image_url", e.message.toString() + " executed ")
            }
            .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                    .totalByteCount
            }
            .addOnCompleteListener {
                Log.e("image_url", it.result.toString())
            }
        return referenceChild
    }

}