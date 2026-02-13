package fr.sdv.b3dev.gameapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import fr.sdv.b3dev.gameapp.domain.Game

sealed class GameDetailUiState {
    object Loading : GameDetailUiState()
    data class Success(val game: Game) : GameDetailUiState()
    data class Error(val message: String) : GameDetailUiState()
}

class GameDetailViewModel(
    private val repository: GameRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Loading)
    val uiState: StateFlow<GameDetailUiState> = _uiState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun fetchGameDetail(gameId: Int, apiKey: String) {
        viewModelScope.launch {
            _uiState.value = GameDetailUiState.Loading
            try {
                val game = repository.getGameDetail(gameId, apiKey)
                _uiState.value = GameDetailUiState.Success(game)
                _isFavorite.value = favoritesRepository.isFavorite(gameId)
            } catch (e: Exception) {
                _uiState.value = GameDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleFavorite(gameId: Int, apiKey: String) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(gameId, apiKey)
            _isFavorite.value = favoritesRepository.isFavorite(gameId)
        }
    }
}
