package fr.sdv.b3dev.gameapp.screens.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fr.sdv.b3dev.gameapp.domain.Game
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.ui.Alignment

@Composable
fun GameDetailContent(game: Game, navController: NavController, context: Context) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = game.background_image,
            contentDescription = game.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Text(text = game.name, style = MaterialTheme.typography.titleLarge)
        Text(text = "⭐ ${game.rating}", style = MaterialTheme.typography.bodyMedium)
        game.esrbRating?.let {
            Text(
                text = "ESRB: $it",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(text = "Released: ${game.released ?: "Unknown"}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Genres: ${game.genres.joinToString { it.name }}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Platforms: ${game.platforms.joinToString { it.name }}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Metacritic: ${game.metacritic ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = game.description ?: "No description available", style = MaterialTheme.typography.bodySmall)

        if (game.developers.isNotEmpty()) {
            Text(
                text = "Developers: ${game.developers.joinToString()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (game.publishers.isNotEmpty()) {
            Text(
                text = "Publishers: ${game.publishers.joinToString()}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        game.website?.let { url ->
            Text(
                text = "Official Website",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                }
            )
        }

        if (game.tags.isNotEmpty()) {

            Text(
                text = "Tags",
                style = MaterialTheme.typography.titleMedium
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                game.tags.forEach { tag ->
                    AssistChip(
                        onClick = { },
                        label = { Text(tag) }
                    )
                }
            }
        }

        if (game.stores.isNotEmpty()) {
            Text(
                text = "Buy on",
                style = MaterialTheme.typography.titleMedium
            )

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

        if (game.screenshots.isNotEmpty()) {

            Text(
                text = "Screenshots",
                style = MaterialTheme.typography.titleMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(game.screenshots) { screenshotUrl ->

                    AsyncImage(
                        model = screenshotUrl,
                        contentDescription = "Screenshot",
                        modifier = Modifier
                            .width(250.dp)
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

            Text(
                text = "Trailer",
                style = MaterialTheme.typography.titleMedium
            )

            val trailerIntentUrl = game.trailerUrl ?: "https://www.youtube.com/results?search_query=${Uri.encode(game.name)}+trailer"

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
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
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

    }
