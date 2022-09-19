package com.bura.chat

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bura.chat.data.room.contacts.Contact
import com.bura.chat.data.room.contacts.ContactDao
import com.bura.chat.data.room.contacts.ContactDatabase
import kotlinx.coroutines.runBlocking
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ContactDaoTest {

    private lateinit var roomDb: ContactDatabase
    private lateinit var contactDao: ContactDao

    @Before
    fun setup() {
        roomDb = Room.databaseBuilder(
            getApplicationContext(),
            ContactDatabase::class.java, "contact_database"
        ).build()

        contactDao = roomDb.contactDao()
    }

    @After
    fun teardown() {
        roomDb.close()
    }

    @Test
    fun insertContact() {
        val contact = Contact(0, "user", "username", "user@gmail.com")

        var containsContact = false
        runBlocking {
            contactDao.insert(contact)
            val allContacts = contactDao.getContactList(contact.user)
            for (contactIndex in allContacts) {
                if (contactIndex.username == contact.username) {
                    containsContact = true
                }
            }
            Assert.assertEquals(true, containsContact)
        }
    }

    @Test
    fun deleteContact() {
        val contact = Contact(0, "user", "username", "user@gmail.com")
        runBlocking {
            contactDao.insert(contact)
            contactDao.delete(contact)

            val allContacts = contactDao.getContactList(contact.user)
            assertThat(allContacts).doesNotContain(contact)
        }
    }
}
