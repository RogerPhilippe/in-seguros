package br.com.inseguros.data.model

import com.google.firebase.firestore.Exclude

data class User(
    var userID: String = "",
    var displayName: String = "",
    var userLogin: String = "",
    var phone: String = "",
    @get:Exclude val passWD: String = ""
)