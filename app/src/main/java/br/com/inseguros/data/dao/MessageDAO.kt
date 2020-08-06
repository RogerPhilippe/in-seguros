package br.com.inseguros.data.dao

import androidx.room.*
import br.com.inseguros.data.model.Message

@Dao
interface MessageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message): Long

    @Update
    suspend fun update(message: Message)

    @Query("SELECT * FROM tb_message")
    suspend fun findAll(): List<Message>

    @Delete
    suspend fun delete(message: Message)

}