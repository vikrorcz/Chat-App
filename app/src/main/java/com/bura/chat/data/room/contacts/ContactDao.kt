package com.bura.chat.data.room.contacts

import androidx.room.*

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Query("SELECT ALL * FROM contact_table WHERE user = :user")
    suspend fun getContactList(user: String): MutableList<Contact>

    @Query("SELECT * FROM contact_table WHERE username = :name AND user = :user")
    suspend fun getContactByName(user: String, name: String) : Contact?

    @Delete
    suspend fun delete(contact: Contact)

    @Update
    suspend fun update(contact: Contact)
}