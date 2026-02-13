package fr.sdv.b3dev.gameapp.datasource.rest

import android.content.Context
import fr.sdv.b3dev.gameapp.domain.FavoriteGame
import fr.sdv.b3dev.gameapp.presentation.GameRepository

class FavoritesRepository(
    private val apiRepository: GameRepository,
    private val fileManager: FavoritesFileManager
) {

    suspend fun toggleFavorite(gameId: Int, apiKey: String) {

        if (fileManager.isFavorite(gameId)) {
            fileManager.removeFavorite(gameId)
            return
        }

        // Fetch full game from API
        val game = apiRepository.getGameDetail(gameId, apiKey)

        // Extract only needed fields
        val favoriteGame = FavoriteGame(
            id = game.id,
            name = game.name,
            description = game.description,
            background_image = game.background_image,
            rating = game.rating,
            released = game.released
        )

        fileManager.addFavorite(favoriteGame)
    }

    fun isFavorite(id: Int): Boolean {
        return fileManager.isFavorite(id)
    }

    fun getFavorites(): List<FavoriteGame> {
        return fileManager.getFavorites()
    }
}


