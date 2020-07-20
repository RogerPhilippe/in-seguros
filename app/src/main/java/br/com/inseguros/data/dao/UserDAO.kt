package br.com.inseguros.data.dao

import androidx.room.*
import br.com.inseguros.data.model.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM tb_user WHERE userID IN (:userID)")
    suspend fun findAllByID(userID: String): User?

    @Delete
    suspend fun delete(user: User)

}