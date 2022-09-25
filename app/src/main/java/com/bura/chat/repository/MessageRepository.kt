package com.bura.chat.repository

import com.bura.chat.data.room.messages.Message
import com.bura.chat.data.room.messages.MessageDao

class MessageRepository(private val messageDao: MessageDao): MessageDao {

    override suspend fun insert(message: Message) {
        messageDao.insert(message)
    }

    override suspend fun getMessageListFromSender(sendingUser: String, receivingUser: String): MutableList<Message> {
        return messageDao.getMessageListFromSender(sendingUser, receivingUser)
    }

    override suspend fun getMessageList(): MutableList<Message>  {
        return messageDao.getMessageList()
    }

    override suspend fun delete(message: Message) {
        messageDao.delete(message)
    }

    override suspend fun update(message: Message)  {
        messageDao.update(message)
    }
}