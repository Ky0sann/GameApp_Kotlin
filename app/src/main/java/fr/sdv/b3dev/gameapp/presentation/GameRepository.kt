package fr.sdv.b3dev.gameapp.presentation

import fr.sdv.b3dev.gameapp.domain.Game
import fr.sdv.b3dev.gameapp.datasource.rest.GameRemoteDataSource

class GameRepository(
    private val remoteDataSource: GameRemoteDataSource
) {

    suspend fun getPopularGames(apiKey: String): List<Game> {
        return remoteDataSource.getPopularGames(apiKey)
    }

    suspend fun getGameDetail(gameId: Int, apiKey: String): Game {
        return remoteDataSource.getGameDetail(gameId, apiKey)
    }
}
