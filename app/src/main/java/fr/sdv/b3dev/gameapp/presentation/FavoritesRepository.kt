package fr.sdv.b3dev.gameapp.presentation
import fr.sdv.b3dev.gameapp.datasource.rest.FavoritesDataSource
import fr.sdv.b3dev.gameapp.domain.FavoriteGame

class FavoritesRepository(
    private val apiRepository: GameRepository,
    private val dataSource: FavoritesDataSource
) {

    suspend fun toggleFavorite(gameId: Int, apiKey: String) {

        if (dataSource.isFavorite(gameId)) {
            val existing = dataSource.getFavorites()
                .first { it.id == gameId }

            dataSource.toggleFavorite(existing)
            return
        }

        val game = apiRepository.getGameDetail(gameId, apiKey)

        val favoriteGame = FavoriteGame(
            id = game.id,
            name = game.name ?: "Unknown",
            description = game.description ?: "",
            background_image = game.background_image ?: "",
            rating = game.rating ?: 0.0,
            released = game.released ?: "Unknown"
        )

        dataSource.toggleFavorite(favoriteGame)
    }

    fun isFavorite(id: Int): Boolean {
        return dataSource.isFavorite(id)
    }

    fun getFavorites(): List<FavoriteGame> {
        return dataSource.getFavorites()
    }
}
