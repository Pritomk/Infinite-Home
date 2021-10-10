package com.example.infinitehome.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.infinitehome.room.UserChat

@Dao
interface UserChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userChat: UserChat)

    @Update
    suspend fun update(userChat: UserChat)

    @Delete
    suspend fun delete(userChat: UserChat)

    @Query("Select * from user_chat_table order by id ASC")
    fun getAllUserChats() : LiveData<List<UserChat>>
}