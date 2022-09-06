package com.bura.chat.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Query("SELECT * FROM contact_table")
    suspend fun getContactList(): MutableList<Contact>

    @Query("SELECT * FROM contact_table WHERE username = :name")
    suspend fun getContactByName(name: String) : Contact?

    @Delete
    suspend fun delete(contact: Contact)

    @Update
    suspend fun update(contact: Contact)
}