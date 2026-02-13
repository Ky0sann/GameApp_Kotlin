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

    suspend fun getFilteredGames(
        apiKey: String,
        query: String? = null,
        genres: List<String>? = null,
        platforms: List<String>? = null,
        tags: List<String>? = null,
        dateRange: Pair<String, String>? = null,
        ordering: String = "-rating",
        pageSize: Int = 20
    ): List<Game> {

        val platformMap = mapOf(
            "PC" to listOf("PC"),
            "PlayStation" to listOf("PlayStation 4", "PlayStation 5"),
            "Xbox" to listOf("Xbox One", "Xbox Series X|S"),
            "Switch" to listOf("Nintendo Switch"),
            "iOS" to listOf("iOS"),
            "Android" to listOf("Android")
        )

        if (genres.isNullOrEmpty() && platforms.isNullOrEmpty() &&
            tags.isNullOrEmpty() && dateRange == null && query.isNullOrBlank()
        ) {
            return getPopularGames(apiKey)
        }

        val datesParam = dateRange?.let { "${it.first},${it.second}" }

        val apiResponse = api.getFilteredGames(
            apiKey = apiKey,
            query = query,
            genres = null,
            platforms = null,
            tags = null,
            dates = datesParam,
            ordering = ordering,
            pageSize = pageSize
        ).results

        val gamesDetailed = apiResponse.map { item ->
            getGameDetail(item.id, apiKey)
        }

        return gamesDetailed.filter { game ->
            val matchesQuery = query.isNullOrBlank() || game.name.contains(query, ignoreCase = true)
            val matchesGenres = genres.isNullOrEmpty() || game.genres.any { it.name in genres }
            val matchesPlatforms = platforms.isNullOrEmpty() || game.platforms.any { gamePlatform ->
                platforms.any { selected ->
                    platformMap[selected]?.any { it.equals(gamePlatform.name, ignoreCase = true) } == true
                }
            }
            val matchesTags = tags.isNullOrEmpty() || game.tags.any { it in tags!! }

            matchesQuery && matchesGenres && matchesPlatforms && matchesTags
        }
    }
}