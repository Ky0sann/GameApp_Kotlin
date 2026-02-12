package fr.sdv.b3dev.gameapp.datasource.rest

import fr.sdv.b3dev.gameapp.domain.Game
import fr.sdv.b3dev.gameapp.domain.Genre
import fr.sdv.b3dev.gameapp.domain.Platform

class GameRemoteDataSource(
    private val api: GameApiService
) {

    suspend fun getPopularGames(apiKey: String): List<Game> {
        return api.getPopularGames(apiKey).results.map { item ->
            Game(
                id = item.id,
                name = item.name,
                background_image = item.backgroundImage,
                rating = item.rating,
                released = item.released
            )
        }
    }

    suspend fun getGameDetail(gameId: Int, apiKey: String): Game {
        val detailResponse = api.getGameDetail(gameId, apiKey)
        val screenshotResponse = api.getGameScreenshots(gameId, apiKey)
        val movieResponse = api.getGameMovies(gameId, apiKey)
        val firstTrailer = movieResponse.results.firstOrNull()

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
            screenshots = screenshotResponse.results.map { it.image },
            developers = detailResponse.developers.map { it.name },
            publishers = detailResponse.publishers.map { it.name },
            website = detailResponse.website,
            tags = detailResponse.tags.map { it.name }.take(10),
            esrbRating = detailResponse.esrb_rating?.name,
            trailerUrl = firstTrailer?.data?.max,
            trailerPreview = firstTrailer?.preview
        )
    }
}
