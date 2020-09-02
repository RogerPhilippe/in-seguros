package br.com.inseguros.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(tableName = "tb_user")
data class User(
    @PrimaryKey var userID: String = "",
    var displayName: String = "",
    var userLogin: String = "",
    var phone: String = "",
    var messagingService: String = "",
    @get:Exclude
    @Ignore
    val passWD: String = ""
)