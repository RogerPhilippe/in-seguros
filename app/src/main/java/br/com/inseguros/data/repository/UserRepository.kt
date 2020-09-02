package br.com.inseguros.data.repository

import br.com.inseguros.data.dao.UserDAO
import br.com.inseguros.data.model.User

class UserRepository(private val mDAO: UserDAO) {

    suspend fun insert(user: User) = mDAO.insert(user)
    suspend fun update(user: User) = mDAO.update(user)
    suspend fun findAllByID(userID: String) = mDAO.findAllByID(userID)
    suspend fun delete(user: User) = mDAO.delete(user)

}