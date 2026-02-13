package fr.sdv.b3dev.gameapp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.getViewModel
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.presentation.GameDetailViewModel

@Composable
fun AppNav(apiKey: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "game_list") {

        // Popular Games
        composable("game_list") {
            val listVM: GameListViewModel = getViewModel()
            GameListScreen(
                apiKey = apiKey,
                viewModel = listVM,
                onGameClick = { gameId ->
                    navController.navigate("game_detail/$gameId")
                }
            )
        }

        // Game Details
        composable(
            route = "game_detail/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
            val detailVM: GameDetailViewModel = getViewModel()
            GameDetailScreen(
                gameId = gameId,
                apiKey = apiKey,
                viewModel = detailVM,
                navController = navController
            )
        }

        // My Favorites
        composable("my_favorites") {
            val detailVM: GameDetailViewModel = getViewModel()
            MyFavoritesScreen(
                viewModel = detailVM,
                onGameClick = { gameId ->
                    navController.navigate("game_detail/$gameId")
                }
            )
        }
    }
}

