package fr.sdv.b3dev.gameapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.screens.components.GameItem
import org.koin.androidx.compose.getViewModel

@Composable
fun GameListScreen(
    viewModel: GameListViewModel = getViewModel(),
    apiKey: String,
    onGameClick: (Int) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.fetchPopularGames(apiKey)
    }

    Scaffold(
        floatingActionButton = {

            IconButton(onClick = { navController.navigate("my_favorites") }) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "My Favorites")
            }


        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
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
                            GameItem(game) {
                                onGameClick(game.id)
                            }
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
}

