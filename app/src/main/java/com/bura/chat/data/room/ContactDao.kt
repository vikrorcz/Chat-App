package com.bura.chat.data.room

import androidx.room.*

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Query("SELECT * FROM contact_table")
    fun getContactList(): MutableList<Contact>

    @Delete
    fun delete(contact: Contact)

    @Update
    fun update(contact: Contact)
}