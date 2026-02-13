package fr.sdv.b3dev.gameapp.datasource.rest

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Display
import androidx.core.content.FileProvider
import fr.sdv.b3dev.gameapp.domain.FavoriteGame
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File

class FavoritesFileManager(private val context: Context) {

    private val fileName = "games_favorites.json"


    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private fun getFile(): File {
        return File(context.getExternalFilesDir(null), fileName)
    }



    private fun readFavorites(): MutableList<FavoriteGame> {
        val file = getFile()

        if (!file.exists()) return mutableListOf()

        val content = file.readText()
        if (content.isBlank()) return mutableListOf()

        return json.decodeFromString(content)
    }

    private fun saveFavorites(list: List<FavoriteGame>) {
        val file = getFile()
        file.writeText(json.encodeToString(list))
        Log.d("FavoritesFileManager", "Saved favorites at: ${file.absolutePath}")
    }

    fun isFavorite(id: Int): Boolean {
        return readFavorites().any { it.id == id }
    }

    fun removeFavorite(id: Int) {
        val updated = readFavorites().filterNot { it.id == id }
        saveFavorites(updated)
    }

    fun addFavorite(game: FavoriteGame) {
        val list = readFavorites()
        list.add(game)
        saveFavorites(list)
    }

    fun getFavorites(): List<FavoriteGame> = readFavorites()
}

