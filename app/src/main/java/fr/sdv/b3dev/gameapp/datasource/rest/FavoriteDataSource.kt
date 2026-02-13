package fr.sdv.b3dev.gameapp.datasource.rest


import android.content.Context
import fr.sdv.b3dev.gameapp.domain.FavoriteGame
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class FavoritesDataSource(private val context: Context) {

    private val fileName = "games_favorites.json"

    private val json = Json {
        prettyPrint  = true
        ignoreUnknownKeys = true
    }

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun getFavorites(): List<FavoriteGame> {
        val file = getFile()
        if (!file.exists()) return emptyList()

        val content = file.readText()
        if (content.isBlank()) return emptyList()

        return json.decodeFromString(content)
    }

    fun saveFavorites(favorites: List<FavoriteGame>) {
        val file = getFile()
        file.writeText(json.encodeToString(favorites))
    }

    fun toggleFavorite(game: FavoriteGame) {
        val currentFavorites = getFavorites().toMutableList()

        val existing = currentFavorites.find { it.id == game.id }

        if (existing != null) {
            currentFavorites.remove(existing)
        } else {
            currentFavorites.add(game)
        }

        saveFavorites(currentFavorites)
    }

    fun isFavorite(id: Int): Boolean {
        return getFavorites().any { it.id == id }
    }
}
