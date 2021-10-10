package com.example.infinitehome.dao

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.example.infinitehome.models.Comment
import com.example.infinitehome.models.Post
import com.example.infinitehome.models.User
import com.example.infinitehome.ui.explore.PostAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class PostDao {

    private val db = FirebaseFirestore.getInstance()
    val postCollection = db.collection("posts")
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

    private fun getPostById(postId: String): Task<DocumentSnapshot> {
        return postCollection.document(postId).get()
    }

    suspend fun updateLikes(postId: String) {
        val currentUserId = auth.currentUser!!.uid
        val post = getPostById(postId).await().toObject(Post::class.java)
        val likedBy = post!!.likedBy
        val isLiked = likedBy.contains(currentUserId)

        if (isLiked) {
            likedBy.remove(currentUserId)
        } else {
            likedBy.add(currentUserId)
        }

        postCollection.document(postId).set(post)

    }

    suspend fun addComment(postId : String, comment: Comment) {
        val post = getPostById(postId).await().toObject(Post::class.java)
        post!!.comments

        post.comments.add(comment)
        postCollection.document(postId).set(post)
    }

    suspend fun getImageUri(postId: String) : Uri {
        val post = getPostById(postId).await().toObject(Post::class.java)
        val storage = Firebase.storage
        val reference = storage.reference.child(post!!.imageReference)
        Log.e("image_uri","Executed1")

        reference.downloadUrl.addOnSuccessListener {
            Log.e("image_uri",it.toString()+"")
        }.addOnFailureListener {
            Log.e("image_uri",it.message+"")
        }.addOnCompleteListener {
            Log.e("image_uri","Complete")
        }

        return reference.downloadUrl.result
    }

    private fun imageUpload(imageUri: Uri): String {
        val storage = Firebase.storage
        val user = Firebase.auth.currentUser!!

        val path: String = user.uid
        val referenceChild = "explore_images/$path/${getTimeDate()}"
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