package fr.sdv.b3dev.gameapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteGame(
    val id: Int,
    val title: String,
    val description: String?,
    val image: String?,
    val rating: Double?,
    val released: String?
)