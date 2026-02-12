package fr.sdv.b3dev.gameapp.datasource.rest

import fr.sdv.b3dev.gameapp.domain.Game
import fr.sdv.b3dev.gameapp.domain.Genre
import fr.sdv.b3dev.gameapp.domain.Platform

class GameRemoteDataSource(
    private val api: GameApiService
) {

    suspend fun getPopularGames(apiKey: String): List<Game> {
        return api.getPopularGames(apiKey).results
    }

    suspend fun getGameDetail(gameId: Int, apiKey: String): Game {
        val detailResponse = api.getGameDetail(gameId, apiKey)
        val screenshotResponse = api.getGameScreenshots(gameId, apiKey)

        return Game(
            id = detailResponse.id,
            name = detailResponse.name,
            background_image = detailResponse.backgroundImage,
            rating = detailResponse.rating,
            released = detailResponse.released,
            description = detailResponse.description_raw,
            metacritic = detailResponse.metacritic,
            genres = detailResponse.genres.map { Genre(it.name) },
            platforms = detailResponse.platforms.map { Platform(it.platform.name) },
            screenshots = screenshotResponse.results.map { it.image }
        )
    }
}
