package fr.sdv.b3dev.gameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.sdv.b3dev.gameapp.screens.AppNav
import fr.sdv.b3dev.gameapp.ui.theme.GameAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameAppTheme {
                AppNav(apiKey = BuildConfig.RAWG_API_KEY)
            }
        }
    }
}
