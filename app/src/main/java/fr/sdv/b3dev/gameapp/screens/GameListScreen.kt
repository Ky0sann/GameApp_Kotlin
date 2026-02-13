package fr.sdv.b3dev.gameapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.presentation.SortOption
import fr.sdv.b3dev.gameapp.screens.components.GameItem
import org.koin.androidx.compose.getViewModel
import fr.sdv.b3dev.gameapp.screens.components.SearchBar

@Composable
fun GameListScreen(
    viewModel: GameListViewModel = getViewModel(),
    apiKey: String,
    onGameClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val sortOption by viewModel.sortOptionState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(apiKey)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        SearchBar(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                viewModel.onSearchQueryChanged(it)
            }
        )

        var expanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            TextButton(onClick = { expanded = true }) {
                Text("Sort by: ${sortOption.displayName}")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                SortOption.values().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.displayName) },
                        onClick = {
                            viewModel.onSortOptionChanged(option)
                            expanded = false
                        }
                    )
                }
            }
        }

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


