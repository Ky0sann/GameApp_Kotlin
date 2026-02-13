package fr.sdv.b3dev.gameapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.sdv.b3dev.gameapp.presentation.GameDetailViewModel
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.screens.components.GameFavoriteItem
import fr.sdv.b3dev.gameapp.screens.components.GameItem
import org.koin.androidx.compose.getViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFavoritesScreen(
    viewModel: GameDetailViewModel = getViewModel(),
    onGameClick: (Int) -> Unit,
    navController : NavController
) {
    // Load favorites when entering screen
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mes favoris") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
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
}
