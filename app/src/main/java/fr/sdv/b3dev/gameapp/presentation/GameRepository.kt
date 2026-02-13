package fr.sdv.b3dev.gameapp.presentation

import fr.sdv.b3dev.gameapp.domain.Game
import fr.sdv.b3dev.gameapp.datasource.rest.GameRemoteDataSource

class GameRepository(
    private val remoteDataSource: GameRemoteDataSource
) {

    private val gameDetailCache = mutableMapOf<Int, Game>()

    suspend fun getPopularGames(apiKey: String): List<Game> {
        return remoteDataSource.getPopularGames(apiKey)
    }

    suspend fun getGameDetail(gameId: Int, apiKey: String): Game {

        gameDetailCache[gameId]?.let {
            return it
        }

        val game = remoteDataSource.getGameDetail(gameId, apiKey)
        gameDetailCache[gameId] = game
        return game
    }

    suspend fun searchGames(apiKey: String, query: String): List<Game> {
        return remoteDataSource.searchGames(apiKey, query)
    }
}
