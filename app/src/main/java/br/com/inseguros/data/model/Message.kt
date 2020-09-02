package br.com.inseguros.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_message")
data class Message(
    @PrimaryKey val id: String = "",
    val status: Boolean = false,
    val messageContentID: Long = 0
)
