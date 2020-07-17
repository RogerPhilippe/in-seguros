package br.com.inseguros.data.model

data class User(
    var userID: String = "",
    var displayName: String = "",
    val userLogin: String = "",
    val passWD: String = ""
)