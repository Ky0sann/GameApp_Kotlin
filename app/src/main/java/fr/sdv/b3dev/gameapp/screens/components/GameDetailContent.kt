package fr.sdv.b3dev.gameapp.screens.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fr.sdv.b3dev.gameapp.domain.Game
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.sdv.b3dev.gameapp.domain.BacklogGame
import fr.sdv.b3dev.gameapp.domain.Status
import fr.sdv.b3dev.gameapp.domain.displayName
import fr.sdv.b3dev.gameapp.presentation.BacklogRepository
import fr.sdv.b3dev.gameapp.presentation.GameDetailViewModel
import fr.sdv.b3dev.gameapp.screens.components.SectionCard

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary)
        content()
    }
}

@Composable
fun GameDetailContent(game: Game, navController: androidx.navigation.NavController, context: Context,  isFavorite: Boolean, onFavoriteClick: () -> Unit, viewModel: GameDetailViewModel) {
    val scrollState = rememberScrollState()
    val currentStatus by viewModel.backlogStatus.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // IMAGE PRINCIPALE
        AsyncImage(
            model = game.background_image,
            contentDescription = game.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        // INFOS PRINCIPALES
        SectionCard(title = "Game Info") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(game.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("⭐ ${game.rating}", style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            game.esrbRating?.let { Text("ESRB: $it", style = MaterialTheme.typography.bodySmall, color = Color.LightGray) }
            Text("Released: ${game.released ?: "Unknown"}", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
            Text("Genres: ${game.genres.joinToString { it.name }}", style = MaterialTheme.typography.bodyMedium)
            Text("Platforms: ${game.platforms.joinToString { it.name }}", style = MaterialTheme.typography.bodyMedium)
            Text("Metacritic: ${game.metacritic ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        }

        // BACKLOG STATUS
        SectionCard(title = "Backlog Status") {
            val options = listOf(Status.TO_PLAY, Status.PLAYING, Status.COMPLETED)
            var expanded by remember { mutableStateOf(false) }

            Box {
                TextButton(onClick = { expanded = true }) {
                    Text(currentStatus?.toString() ?: "Select Status")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status.displayName()) },
                            onClick = {
                                viewModel.updateBacklogStatus(game.id, game.name, status)
                                expanded = false
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text("Remove Status") },
                        onClick = {
                            viewModel.removeBacklogStatus(game.id)
                            expanded = false
                        }
                    )
                }
            }
        }

        // DESCRIPTION
        if (!game.description.isNullOrEmpty()) {
            SectionCard(title = "Description") {
                Text(text = game.description, style = MaterialTheme.typography.bodySmall)
            }
        }

        // DEVELOPERS & PUBLISHERS
        if (game.developers.isNotEmpty() || game.publishers.isNotEmpty()) {
            SectionCard(title = "Developers & Publishers") {
                if (game.developers.isNotEmpty()) Text("Developers: ${game.developers.joinToString()}", style = MaterialTheme.typography.bodyMedium)
                if (game.publishers.isNotEmpty()) Text("Publishers: ${game.publishers.joinToString()}", style = MaterialTheme.typography.bodyMedium)
            }
        }

        // SITE OFFICIEL
        game.website?.let { url ->
            SectionCard(title = "Official Website") {
                Text(
                    text = url,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    }
                )
            }
        }

        // TAGS
        if (game.tags.isNotEmpty()) {
            SectionCard(title = "Tags") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    game.tags.forEach { tag ->
                        AssistChip(onClick = { }, label = { Text(tag) })
                    }
                }
            }
        }

        // STORES
        if (game.stores.isNotEmpty()) {
            SectionCard(title = "Buy On") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    game.stores.forEach { store ->
                        AssistChip(
                            onClick = {
                                val url = "https://${store.domain}/search?q=${Uri.encode(game.name)}"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            label = { Text(store.name) }
                        )
                    }
                }
            }
        }

        // SCREENSHOTS
        if (game.screenshots.isNotEmpty()) {
            SectionCard(title = "Screenshots") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(game.screenshots) { screenshotUrl ->
                        AsyncImage(
                            model = screenshotUrl,
                            contentDescription = "Screenshot",
                            modifier = Modifier
                                .width(250.dp)
                                .height(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // TRAILER
        SectionCard(title = "Trailer") {
            val trailerIntentUrl = game.trailerUrl
                ?: "https://www.youtube.com/results?search_query=${Uri.encode(game.name)}+trailer"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerIntentUrl))
                        context.startActivity(intent)
                    }
            ) {
                AsyncImage(
                    model = game.trailerPreview ?: "https://changeourcity.com/wp-content/uploads/2020/03/watch-on-youtube-vbf.png",
                    contentDescription = "Trailer Preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "▶",
                    style = MaterialTheme.typography.displayMedium.copy(color = Color.White),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
