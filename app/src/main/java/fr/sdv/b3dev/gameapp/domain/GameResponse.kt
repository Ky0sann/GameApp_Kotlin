package fr.sdv.b3dev.gameapp.domain

import com.google.gson.annotations.SerializedName

data class GameResponse(
    val results: List<GameRemoteListItem>
)

data class ScreenshotResponse(
    val results: List<ScreenshotResponseItem>
)

data class ScreenshotResponseItem(
    val image: String
)

data class CompanyResponse(
    val name: String
)

data class TagResponse(
    val name: String
)

data class EsrbResponse(
    val name: String
)

data class GameRemoteListItem(
    val id: Int,
    val name: String,

    @SerializedName("background_image")
    val backgroundImage: String?,

    val rating: Double,
    val released: String?
)

data class MovieResponse(
    val results: List<MovieItem>
)
