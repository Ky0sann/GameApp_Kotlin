package fr.sdv.b3dev.gameapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class BacklogGame(
    val id: Int,
    val title: String,
    val status: Status
)

@Serializable
enum class Status {
    TO_PLAY,
    PLAYING,
    COMPLETED
}

fun Status.displayName() = when(this) {
    Status.TO_PLAY -> "To Play"
    Status.PLAYING -> "Playing"
    Status.COMPLETED -> "Completed"
}