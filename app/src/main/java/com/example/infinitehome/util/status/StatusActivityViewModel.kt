package com.example.infinitehome.util.status

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.infinitehome.models.Status
import com.google.firebase.firestore.FirebaseFirestore

class StatusActivityViewModel(uid: String) : ViewModel() {
    val _references: MutableLiveData<ArrayList<Status>> = MutableLiveData<ArrayList<Status>>()
    private val TAG = "com.example.infinitehome.util.status"

    init {
        fetchingStatusReferences(uid)
    }

    private fun fetchingStatusReferences(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("status")
        collection
            .document(uid)
            .collection("status")
            .addSnapshotListener{snapshot,e ->
                if (e != null) {
                    Log.e(TAG,e.message.toString())
                }
                if (snapshot != null) {
                    val references = ArrayList<Status>()
                    val documets = snapshot.documents
                    documets.forEach {
                        val reference = it.toObject(Status::class.java)
                        if (reference != null) {
                            references.add(reference)
                        }
                    }
                    Log.e(TAG,"$references")
                    _references.value = references
                }
            }
    }
}