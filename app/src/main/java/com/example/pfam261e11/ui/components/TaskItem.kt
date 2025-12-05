package com.example.pfam261e11.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pfam261e11.models.Task

@Composable
fun TaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            //     IMAGEN (si existe)
            if (task.imageUri != null) {
                AsyncImage(
                    model = task.imageUri,
                    contentDescription = "Imagen de tarea",
                    modifier = Modifier
                        .size(55.dp)
                        .padding(end = 10.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick() }
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge
                )

                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }

            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { newValue ->
                    onCheckedChange(newValue)
                }
            )
        }
    }
}
