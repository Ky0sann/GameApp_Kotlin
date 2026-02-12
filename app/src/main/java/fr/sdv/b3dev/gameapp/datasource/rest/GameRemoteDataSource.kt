package fr.sdv.b3dev.gameapp.datasource.rest

import fr.sdv.b3dev.gameapp.domain.Game
class GameRemoteDataSource(
    private val api: GameApiService
) {

    suspend fun getPopularGames(apiKey: String): List<Game> {
        return api.getPopularGames(apiKey).results
    }
}
