package fr.sdv.b3dev.gameapp.domain

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String
)