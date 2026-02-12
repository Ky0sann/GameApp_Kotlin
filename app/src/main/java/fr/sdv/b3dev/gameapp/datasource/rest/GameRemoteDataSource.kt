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
        val response = api.getGameDetail(gameId, apiKey)

        return Game(
            id = response.id,
            name = response.name,
            background_image = response.backgroundImage,
            rating = response.rating,
            released = response.released,
            description = response.description_raw,
            metacritic = response.metacritic,
            genres = response.genres.map { Genre(it.name) },
            platforms = response.platforms.map { Platform(it.platform.name) }
        )
    }
}
