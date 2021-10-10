package com.example.infinitehome.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_chat_table",indices = [Index(value = ["uid"], unique = true)])
data class UserChat(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    val displayName : String = "",
    val imageUrl : String = "",
    val uid : String = ""
)
