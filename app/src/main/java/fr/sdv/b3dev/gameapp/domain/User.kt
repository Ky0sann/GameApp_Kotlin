package fr.sdv.b3dev.gameapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String,
    val email: String,
    val password: String
)