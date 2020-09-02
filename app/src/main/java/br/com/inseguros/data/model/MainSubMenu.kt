package br.com.inseguros.data.model

import java.io.Serializable

data class MainSubMenu(
    val id: String = "",
    val title: String = "",
    val subTitle: String = "",
    val description: String = "",
    val menuIcon: String = ""
): Serializable