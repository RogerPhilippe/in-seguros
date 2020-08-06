package br.com.inseguros.data.repository

import br.com.inseguros.data.dao.MessageDAO
import br.com.inseguros.data.model.Message

class MessageRepository(private val mDAO: MessageDAO) {

    suspend fun insert(message: Message) = mDAO.insert(message)
    suspend fun update(message: Message) = mDAO.update(message)
    suspend fun findAll() = mDAO.findAll()
    suspend fun delete(message: Message) = mDAO.delete(message)

}