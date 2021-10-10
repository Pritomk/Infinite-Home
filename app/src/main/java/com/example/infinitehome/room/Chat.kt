package com.example.infinitehome.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table" , indices = [Index(value = ["timeStamp"], unique = true)])
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String = "",
    val type: String = "",
    val message: String = "",
    val timeStamp: Long = 0L
)
