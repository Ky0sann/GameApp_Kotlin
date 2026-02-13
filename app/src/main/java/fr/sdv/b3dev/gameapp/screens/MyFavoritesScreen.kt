package fr.sdv.b3dev.gameapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.sdv.b3dev.gameapp.presentation.GameDetailViewModel
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.screens.components.GameFavoriteItem
import fr.sdv.b3dev.gameapp.screens.components.GameItem
import org.koin.androidx.compose.getViewModel


@Composable
fun MyFavoritesScreen(
    viewModel: GameDetailViewModel = getViewModel(),
    onGameClick: (Int) -> Unit
) {
    // Load favorites when entering screen
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favorites by viewModel.favorites.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No favorites yet!", color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(favorites) { game ->
                    GameFavoriteItem(game) {
                        onGameClick(game.id)
                    }
                }
            }
        }
    }
}
