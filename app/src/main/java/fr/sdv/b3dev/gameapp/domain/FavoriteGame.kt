package fr.sdv.b3dev.gameapp.domain

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteGame(
    val id: Int,
    val name: String,
    val description: String?,
    val background_image: String?,
    val rating: Double?,
    val released: String?
)