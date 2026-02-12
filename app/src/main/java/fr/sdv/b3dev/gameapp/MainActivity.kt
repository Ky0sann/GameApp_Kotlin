package fr.sdv.b3dev.gameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.sdv.b3dev.gameapp.ui.theme.GameAppTheme
import fr.sdv.b3dev.gameapp.datasource.rest.ApiModule
import fr.sdv.b3dev.gameapp.datasource.rest.GameRemoteDataSource
import fr.sdv.b3dev.gameapp.presentation.GameRepository
import fr.sdv.b3dev.gameapp.presentation.GameListViewModel
import fr.sdv.b3dev.gameapp.screens.GameListScreen

class MainActivity : ComponentActivity() {

    private val viewModel by lazy {
        val api = ApiModule.api
        val remoteDataSource = GameRemoteDataSource(api)
        val repository = GameRepository(remoteDataSource)
        GameListViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameAppTheme {
                GameListScreen(
                    viewModel = viewModel,
                    apiKey = BuildConfig.RAWG_API_KEY
                )
            }
        }
    }
}
