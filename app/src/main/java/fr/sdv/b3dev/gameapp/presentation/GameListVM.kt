package fr.sdv.b3dev.gameapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.gameapp.domain.Game
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

    private val _sortOption = MutableStateFlow(SortOption.RATING_DESC)
    val sortOptionState: StateFlow<SortOption> = _sortOption

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

    fun onSortOptionChanged(option: SortOption) {
        _sortOption.value = option

        val currentState = _uiState.value
        if (currentState is GameListUiState.Success) {
            _uiState.value = GameListUiState.Success(
                sortGames(currentState.games, option)
            )
        }
    }

    private fun observeSearch() {
        viewModelScope.launch {
            searchQuery
                .debounce(700)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.length in 1..2) return@collectLatest
                    val apiKey = currentApiKey ?: return@collectLatest

                    _uiState.value = GameListUiState.Loading

                    try {
                        val games = if (query.isBlank()) {
                            repository.getPopularGames(apiKey)
                        } else {
                            repository.searchGames(apiKey, query)
                        }

                        val sortedGames = sortGames(games, _sortOption.value)
                        _uiState.value = GameListUiState.Success(sortedGames)

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
                val sortedGames = sortGames(games, _sortOption.value)
                _uiState.value = GameListUiState.Success(sortedGames)
            } catch (e: Exception) {
                _uiState.value =
                    GameListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun sortGames(games: List<Game>, option: SortOption): List<Game> {
        return when (option) {
            SortOption.NAME_ASC -> games.sortedBy { it.name }
            SortOption.NAME_DESC -> games.sortedByDescending { it.name }
            SortOption.RATING_ASC -> games.sortedBy { it.rating }
            SortOption.RATING_DESC -> games.sortedByDescending { it.rating }
            SortOption.RELEASED_ASC -> games.sortedBy { it.released ?: "" }
            SortOption.RELEASED_DESC -> games.sortedByDescending { it.released ?: "" }
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
