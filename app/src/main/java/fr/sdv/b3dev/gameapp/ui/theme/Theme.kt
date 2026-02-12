package fr.sdv.b3dev.gameapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6C5CE7),
    onPrimary = Color.White,
    secondary = Color(0xFF00CEC9),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

@Composable
fun GameAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(),
        content = content
    )
}
