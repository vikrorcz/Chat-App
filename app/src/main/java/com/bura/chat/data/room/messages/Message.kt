package com.bura.chat.data.room.messages

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message_table")
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name= "message")
    var message: String,
    @ColumnInfo(name = "sendingUser")
    var sendingUser: String,
    @ColumnInfo(name = "receivingUser")
    var receivingUser: String
)

