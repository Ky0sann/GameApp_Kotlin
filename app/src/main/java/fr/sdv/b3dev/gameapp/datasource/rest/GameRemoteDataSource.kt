package fr.sdv.b3dev.gameapp.datasource.rest

import fr.sdv.b3dev.gameapp.domain.Game
import fr.sdv.b3dev.gameapp.domain.Genre
import fr.sdv.b3dev.gameapp.domain.Platform
import fr.sdv.b3dev.gameapp.domain.Store
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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

    suspend fun searchGames(apiKey: String, query: String): List<Game> {
        return api.searchGames(apiKey, query).results.map { item ->
            Game(
                id = item.id,
                name = item.name,
                background_image = item.backgroundImage,
                rating = item.rating,
                released = item.released
            )
        }
    }

    suspend fun getGameDetail(gameId: Int, apiKey: String): Game = coroutineScope {

        val detailDeferred = async { api.getGameDetail(gameId, apiKey) }
        val screenshotsDeferred = async { api.getGameScreenshots(gameId, apiKey) }
        val moviesDeferred = async { api.getGameMovies(gameId, apiKey) }

        val detailResponse = detailDeferred.await()
        val screenshotResponse = screenshotsDeferred.await()
        val movieResponse = moviesDeferred.await()

        val firstTrailer = movieResponse.results.firstOrNull()
        val stores = detailResponse.stores.map { Store(it.store.name, it.store.domain) }

        Game(
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
            trailerPreview = firstTrailer?.preview,
            stores = stores
        )
    }
}
