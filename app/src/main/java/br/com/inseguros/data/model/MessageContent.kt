package br.com.inseguros.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_messageContent")
data class MessageContent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quoteID: String = "",
    val company: String = "",
    val title: String = "",
    val bodyText: String = "",
    val proposalID: String = ""
)