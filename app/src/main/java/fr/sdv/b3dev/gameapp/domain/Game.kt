package fr.sdv.b3dev.gameapp.domain

data class Game(
    val id: Int,
    val name: String,
    val background_image: String?,
    val rating: Double,
    val released: String?
)
