package fr.sdv.b3dev.gameapp.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items

@Composable
fun FilterChipsRow(
    title: String,
    options: List<String>,
    selectedOptions: List<String>,
    onSelectionChange: (List<String>) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            items(options) { option ->
                val isSelected = selectedOptions.contains(option)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        val newList = selectedOptions.toMutableList()
                        if (isSelected) newList.remove(option) else newList.add(option)
                        onSelectionChange(newList)
                    },
                    label = { Text(option) }
                )
            }
        }
    }
}
