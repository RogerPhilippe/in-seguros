package br.com.inseguros.data.dao

import androidx.room.*
import br.com.inseguros.data.model.MessageContent

@Dao
interface MessageContentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messageContent: MessageContent): Long

    @Update
    suspend fun update(messageContent: MessageContent)

    @Query("SELECT * FROM tb_messageContent")
    suspend fun findAll(): List<MessageContent>

    @Delete
    suspend fun delete(messageContent: MessageContent)

}