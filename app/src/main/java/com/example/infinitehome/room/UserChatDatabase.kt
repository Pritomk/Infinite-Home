package com.example.infinitehome.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.infinitehome.dao.UserChatDao

@Database(entities = [UserChat::class], version = 1, exportSchema = false)
abstract class UserChatDatabase : RoomDatabase() {

    abstract fun getUserChatDao() : UserChatDao

    companion object {
        @Volatile
        private var INSTANCE: UserChatDatabase? = null

        fun getDatabase(context: Context): UserChatDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserChatDatabase::class.java,
                    "user_chat_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}