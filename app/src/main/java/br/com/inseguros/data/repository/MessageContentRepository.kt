package br.com.inseguros.data.repository

import br.com.inseguros.data.dao.MessageContentDAO
import br.com.inseguros.data.model.MessageContent

class MessageContentRepository(private val mDAO: MessageContentDAO) {

    suspend fun insert(messageContent: MessageContent) = mDAO.insert(messageContent)
    suspend fun update(messageContent: MessageContent) = mDAO.update(messageContent)
    suspend fun findAll() = mDAO.findAll()
    suspend fun delete(messageContent: MessageContent) = mDAO.delete(messageContent)

}