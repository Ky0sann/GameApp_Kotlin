package fr.sdv.b3dev.gameapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import fr.sdv.b3dev.gameapp.presentation.GameDetailUiState
import fr.sdv.b3dev.gameapp.presentation.GameDetailViewModel
import fr.sdv.b3dev.gameapp.screens.components.GameDetailContent
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    gameId: Int,
    apiKey: String,
    navController: NavController,
    viewModel: GameDetailViewModel = getViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(gameId) {
        viewModel.fetchGameDetail(gameId, apiKey)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (uiState) {
                is GameDetailUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is GameDetailUiState.Success -> {
                    val game = (uiState as GameDetailUiState.Success).game
                    GameDetailContent(game, navController, context)
                }
                is GameDetailUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (uiState as GameDetailUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}