package com.bura.chat.data.room.contacts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "user")//user, who is currently using the app
    var user:String,
    @ColumnInfo(name = "username")//contact's username
    var username:String,
    @ColumnInfo(name = "email")//contact's email
    var email: String
)

