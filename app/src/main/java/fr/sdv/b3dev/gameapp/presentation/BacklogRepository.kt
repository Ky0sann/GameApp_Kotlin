package fr.sdv.b3dev.gameapp.presentation

import fr.sdv.b3dev.gameapp.datasource.rest.BacklogDataSource
import fr.sdv.b3dev.gameapp.domain.Status

class BacklogRepository(private val dataSource: BacklogDataSource) {

    fun getAll() = dataSource.getAll()
    fun getStatus(gameId: Int) = dataSource.getStatus(gameId)
    fun setStatus(gameId: Int, title: String, status: Status) = dataSource.setStatus(gameId, title, status)
    fun remove(gameId: Int) = dataSource.remove(gameId)
}