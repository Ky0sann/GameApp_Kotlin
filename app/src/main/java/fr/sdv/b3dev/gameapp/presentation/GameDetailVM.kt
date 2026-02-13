package fr.sdv.b3dev.gameapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.gameapp.domain.FavoriteGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import fr.sdv.b3dev.gameapp.domain.Game
import fr.sdv.b3dev.gameapp.domain.Status

sealed class GameDetailUiState {
    object Loading : GameDetailUiState()
    data class Success(val game: Game) : GameDetailUiState()
    data class Error(val message: String) : GameDetailUiState()
}

class GameDetailViewModel(
    private val repository: GameRepository,
    private val favoritesRepository: FavoritesRepository,
    private val backlogRepository: BacklogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Loading)
    val uiState: StateFlow<GameDetailUiState> = _uiState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    private val _backlogStatus = MutableStateFlow<Status?>(null)
    val backlogStatus: StateFlow<Status?> = _backlogStatus

    private val _favorites = MutableStateFlow<List<FavoriteGame>>(emptyList())
    val favorites: StateFlow<List<FavoriteGame>> = _favorites


    fun fetchGameDetail(gameId: Int, apiKey: String) {
        viewModelScope.launch {
            _uiState.value = GameDetailUiState.Loading
            try {
                val game = repository.getGameDetail(gameId, apiKey)
                _uiState.value = GameDetailUiState.Success(game)
                _isFavorite.value = favoritesRepository.isFavorite(gameId)
                fetchBacklogStatus(gameId)
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

    fun fetchBacklogStatus(gameId: Int) {
        _backlogStatus.value = backlogRepository.getStatus(gameId)
    }

    fun updateBacklogStatus(gameId: Int, title: String, status: Status) {
        backlogRepository.setStatus(gameId, title, status)
        _backlogStatus.value = status
    }

    fun removeBacklogStatus(gameId: Int) {
        backlogRepository.remove(gameId)
        _backlogStatus.value = null
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = favoritesRepository.getFavorites()
        }
    }

}
