package com.example.infinitehome.repositories

import android.net.Uri
import com.example.infinitehome.dao.StatusDao

class StatusRepository {
    private val statusDao = StatusDao()
    fun addStatus(imageUri: Uri) {
        statusDao.addStatus(imageUri)
    }
}