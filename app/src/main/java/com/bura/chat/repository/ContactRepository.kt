package com.bura.chat.repository

import com.bura.chat.data.room.contacts.Contact
import com.bura.chat.data.room.contacts.ContactDao

class ContactRepository(private val contactDao: ContactDao) : ContactDao {
    override suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    override suspend fun getContactList(user: String): MutableList<Contact> {
        return contactDao.getContactList(user)
    }

    override suspend fun getContactByName(user: String, name: String) : Contact? {
        return contactDao.getContactByName(user, name)
    }

    override suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

    override suspend fun update(contact: Contact) {
        contactDao.update(contact)
    }
}