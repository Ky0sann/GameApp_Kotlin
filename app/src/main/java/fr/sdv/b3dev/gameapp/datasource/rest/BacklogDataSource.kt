package fr.sdv.b3dev.gameapp.datasource.rest

import android.content.Context
import fr.sdv.b3dev.gameapp.domain.BacklogGame
import fr.sdv.b3dev.gameapp.domain.Status
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class BacklogDataSource(private val context: Context) {

    private val fileName = "games_backlog.json"
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    private fun getFile(): File = File(context.filesDir, fileName)

    private fun readBacklog(): MutableList<BacklogGame> {
        val file = getFile()
        if (!file.exists()) return mutableListOf()
        val content = file.readText()
        if (content.isBlank()) return mutableListOf()
        return json.decodeFromString(content)
    }

    private fun saveBacklog(list: List<BacklogGame>) {
        getFile().writeText(json.encodeToString(list))
    }

    fun getAll(): List<BacklogGame> = readBacklog()

    fun getStatus(id: Int): Status? = readBacklog().firstOrNull { it.id == id }?.status

    fun setStatus(gameId: Int, title: String, status: Status) {
        val backlog = readBacklog()
        val index = backlog.indexOfFirst { it.id == gameId }
        if (index != -1) {
            backlog[index] = backlog[index].copy(status = status)
        } else {
            backlog.add(BacklogGame(gameId, title, status))
        }
        saveBacklog(backlog)
    }

    fun remove(gameId: Int) {
        val backlog = readBacklog().toMutableList()
        backlog.removeAll { it.id == gameId }
        saveBacklog(backlog)
    }
}