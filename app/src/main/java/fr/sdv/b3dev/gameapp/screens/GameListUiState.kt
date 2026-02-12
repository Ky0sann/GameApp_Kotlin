package fr.sdv.b3dev.gameapp.screens

import fr.sdv.b3dev.gameapp.domain.Game

sealed class GameListUiState {
    object Loading : GameListUiState()
    data class Success(val games: List<Game>) : GameListUiState()
    data class Error(val message: String) : GameListUiState()
}
