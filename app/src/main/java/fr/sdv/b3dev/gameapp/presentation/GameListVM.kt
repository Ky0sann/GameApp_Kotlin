package fr.sdv.b3dev.gameapp.presentation

import androidx.lifecycle.ViewModel import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.gameapp.presentation.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import fr.sdv.b3dev.gameapp.screens.GameListUiState

class GameListViewModel(
    private val repository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameListUiState>(GameListUiState.Loading)
    val uiState: StateFlow<GameListUiState> = _uiState

    fun fetchPopularGames(apiKey: String) {
        viewModelScope.launch {
            _uiState.value = GameListUiState.Loading
            try {
                val games = repository.getPopularGames(apiKey)
                _uiState.value = GameListUiState.Success(games)
            } catch (e: Exception) {
                _uiState.value = GameListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
