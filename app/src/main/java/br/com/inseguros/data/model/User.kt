package br.com.inseguros.data.model

data class User(
    var userID: String = "",
    var displayName: String = "",
    var userLogin: String = "",
    val passWD: String = ""
)