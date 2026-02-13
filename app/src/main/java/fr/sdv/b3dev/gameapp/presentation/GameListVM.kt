package fr.sdv.b3dev.gameapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import fr.sdv.b3dev.gameapp.screens.GameListUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

class GameListViewModel(
    private val repository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameListUiState>(GameListUiState.Loading)
    val uiState: StateFlow<GameListUiState> = _uiState

    private val searchQuery = MutableStateFlow("")
    private var currentApiKey: String? = null

    init {
        observeSearch()
    }

    fun init(apiKey: String) {
        if (currentApiKey == null) {
            currentApiKey = apiKey
            fetchPopularGames()
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(700)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.length in 1..2) return@collectLatest
                    val apiKey = currentApiKey ?: return@collectLatest

                    if (_uiState.value !is GameListUiState.Success) {
                        _uiState.value = GameListUiState.Loading
                    }

                    try {
                        val games = if (query.isBlank()) {
                            repository.getPopularGames(apiKey)
                        } else {
                            repository.searchGames(apiKey, query)
                        }

                        _uiState.value = GameListUiState.Success(games)

                    } catch (e: Exception) {
                        _uiState.value =
                            GameListUiState.Error(e.message ?: "Unknown error")
                    }
                }
        }
    }

    private fun fetchPopularGames() {
        val apiKey = currentApiKey ?: return

        viewModelScope.launch {
            _uiState.value = GameListUiState.Loading
            try {
                val games = repository.getPopularGames(apiKey)
                _uiState.value = GameListUiState.Success(games)
            } catch (e: Exception) {
                _uiState.value =
                    GameListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun performSearch(query: String) {
        val apiKey = currentApiKey ?: return

        viewModelScope.launch {
            _uiState.value = GameListUiState.Loading
            try {
                val games = repository.searchGames(apiKey, query)
                _uiState.value = GameListUiState.Success(games)
            } catch (e: Exception) {
                _uiState.value =
                    GameListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
