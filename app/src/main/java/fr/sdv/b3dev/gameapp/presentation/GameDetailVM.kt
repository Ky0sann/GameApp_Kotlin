package fr.sdv.b3dev.gameapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.gameapp.BuildConfig
import fr.sdv.b3dev.gameapp.datasource.rest.FavoritesRepository
import fr.sdv.b3dev.gameapp.domain.FavoriteGame
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
    private val favoritesRepository: FavoritesRepository // Inject FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Loading)
    val uiState: StateFlow<GameDetailUiState> = _uiState

    private val _favorites = MutableStateFlow<List<FavoriteGame>>(emptyList())
    val favorites: StateFlow<List<FavoriteGame>> = _favorites

    fun fetchGameDetail(gameId: Int, apiKey: String) {
        viewModelScope.launch {
            _uiState.value = GameDetailUiState.Loading
            try {
                val game = repository.getGameDetail(gameId, apiKey)
                _uiState.value = GameDetailUiState.Success(game)
            } catch (e: Exception) {
                _uiState.value = GameDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // -------------------------
    // FAVORITES
    // -------------------------

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = favoritesRepository.getFavorites()
        }
    }

    fun toggleFavorite(gameId: Int) {
        val apiKey = BuildConfig.RAWG_API_KEY
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(gameId, apiKey)
            loadFavorites()
        }
    }

    fun isFavorite(id: Int): Boolean {
        return _favorites.value.any { it.id == id }
    }
}

