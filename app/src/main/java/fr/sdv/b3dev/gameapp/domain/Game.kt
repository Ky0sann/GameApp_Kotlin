package fr.sdv.b3dev.gameapp.domain

data class Game(
    val id: Int,
    val name: String,
    val background_image: String?,
    val rating: Double,
    val released: String?,
    val genres: List<Genre> = emptyList(),
    val platforms: List<Platform> = emptyList(),
    val metacritic: Int? = null,
    val description: String? = null,
    val screenshots: List<String> = emptyList(),
    val developers: List<String> = emptyList(),
    val publishers: List<String> = emptyList(),
    val website: String? = null,
    val tags: List<String> = emptyList(),
    val esrbRating: String? = null
)

data class Genre(val name: String)
data class Platform(val name: String)