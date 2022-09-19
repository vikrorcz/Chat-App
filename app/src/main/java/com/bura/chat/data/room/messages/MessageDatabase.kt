package com.bura.chat.data.room.messages

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}