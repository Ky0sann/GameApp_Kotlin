package fr.sdv.b3dev.gameapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.screens.components.GameItem

@Composable
fun GameListScreen(
    viewModel: GameListViewModel,
    apiKey: String
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPopularGames(apiKey)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        when (uiState) {

            is GameListUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is GameListUiState.Success -> {
                val games = (uiState as GameListUiState.Success).games

                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(games) { game ->
                        GameItem(game)
                    }
                }
            }

            is GameListUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as GameListUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

