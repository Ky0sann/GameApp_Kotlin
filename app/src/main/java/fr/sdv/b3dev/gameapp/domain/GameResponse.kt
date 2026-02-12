package fr.sdv.b3dev.gameapp.domain

data class GameResponse(
    val results: List<Game>
)

data class ScreenshotResponse(
    val results: List<ScreenshotResponseItem>
)

data class ScreenshotResponseItem(
    val image: String
)
