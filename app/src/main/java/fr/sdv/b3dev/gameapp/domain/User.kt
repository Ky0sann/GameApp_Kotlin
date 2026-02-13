package fr.sdv.b3dev.gameapp.domain
/*
import androidx.room.Entity
import androidx.room.PrimaryKey
*/

// @Entity(tableName = "users")

data class User(
    val id: Long,
    val username: String,
    val email: String,
    val password: String
)