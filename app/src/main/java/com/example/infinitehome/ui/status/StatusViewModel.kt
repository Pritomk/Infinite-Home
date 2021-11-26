package com.example.infinitehome.ui.status

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinitehome.models.User
import com.example.infinitehome.repositories.StatusRepository
import com.google.firebase.firestore.FirebaseFirestore

class StatusViewModel(uId: String) : ViewModel() {
    val _userIds: MutableLiveData<ArrayList<User>> = MutableLiveData()
    private val TAG = "com.example.infinitehome.ui.status.StatusViewModel"
    private val db = FirebaseFirestore.getInstance()

    init {
        fetchingStatusList(uId)
    }

    private fun fetchingStatusList(uId: String) {
        db.collection("chats")
            .document(uId)
            .collection("messages")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e(TAG, e.message.toString())
                }
                if (snapshot != null) {
                    val users = ArrayList<User>()

                    val documents = snapshot.documents
                    for (document in documents) {
                        Log.d(TAG,"Executed")
                        fetchDocument(document.id)
                    }
                }
            }
    }

    private fun fetchDocument(uId : String ) {
        val docRef = db.collection("users").document(uId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val u = _userIds.value
                    val users = ArrayList<User>()
                    if (u != null) {
                        users.addAll(u)
                    }
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                    _userIds.value = users
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private val statusRepository = StatusRepository()
    fun addStatus(imageUri: Uri) {
        statusRepository.addStatus(imageUri)
    }


}