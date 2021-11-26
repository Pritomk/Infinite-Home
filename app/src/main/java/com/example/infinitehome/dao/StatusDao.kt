package com.example.infinitehome.dao

import android.net.Uri
import android.util.Log
import com.example.infinitehome.models.Status
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class StatusDao {
    private val db = FirebaseFirestore.getInstance();
    private val collection = db.collection("status")
    private val auth = Firebase.auth

    fun addStatus(imageUri: Uri) {
        val currentTime = System.currentTimeMillis()
        val imageReference = imageUpload(imageUri)
        val status = Status(imageReference,currentTime)
        val cid = auth.currentUser!!.uid
        Log.e("add_status",imageReference)
        collection
            .document(cid)
            .collection("status")
            .document().set(status)
    }

    private fun imageUpload(imageUri: Uri): String {
        val storage = Firebase.storage
        val user = Firebase.auth.currentUser!!

        val path: String = user.uid
        val referenceChild = "status_images/$path/${getTimeDate()}"
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