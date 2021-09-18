package com.example.infinitehome.dao

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.infinitehome.models.Post
import com.example.infinitehome.models.User
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    private val postCollection = db.collection("posts")
    private val auth = Firebase.auth
    private lateinit var context: Context

    suspend fun addPost(proContext: Context, imageUri: Uri, text: String) {
        context = proContext
        val currentUserID = auth.currentUser!!.uid
        val referenceChild = imageUpload(imageUri)
        val userDao = UserDao()
        val user = userDao.getUserId(currentUserID).await().toObject(User::class.java)!!
        val currentTime = System.currentTimeMillis()
        val post = Post(referenceChild, text, user, currentTime)
        postCollection.document().set(post)
    }

    fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }

    suspend fun updateLikes(postId: String) {
        val currentUserId = auth.currentUser!!.uid
        val post = getPostById(postId).await().toObject(Post::class.java)
        val isLiked = post!!.likedBy.contains(currentUserId)

        if (isLiked) {
            post.likedBy.remove(currentUserId)
        } else {
            post.likedBy.add(currentUserId)
        }

        postCollection.document(postId).set(post)

    }


    private fun imageUpload(imageUri: Uri): String {
        val storage = Firebase.storage
        val user = Firebase.auth.currentUser!!

        val path: String = user.uid
        val referenceChild = "images/$path/${getTimeDate()}"
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

    private fun getTimeDate(): String {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        return "$currentDate/$currentTime"
    }
}