package com.bura.chat.data.room.messages

import androidx.room.*

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message)

    @Query("SELECT ALL * FROM message_table WHERE sendingUser= :sendingUser AND receivingUser = :receivingUser")//gets messages from sender and also the user
    suspend fun getMessageListFromSender(sendingUser: String, receivingUser: String): MutableList<Message>

    @Query("SELECT ALL * FROM message_table")
    suspend fun getMessageList(): MutableList<Message>

    @Delete
    suspend fun delete(message: Message)

    @Update
    suspend fun update(message: Message)
}