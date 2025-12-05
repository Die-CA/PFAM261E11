package com.example.pfam261e11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pfam261e11.models.Task
import com.example.pfam261e11.ui.components.TaskItem

@Composable
fun HomeScreen(
    tasks: List<Task>,
    onAddClick: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    onTaskCompleted: (Int, Boolean) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            Text(
                text = "Mis tareas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onSettingsClick) {
                    Text("Ajustes")
                }
                Button(onClick = onAboutClick) {
                    Text("Acerca de")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(tasks) { index, task ->
                    TaskItem(
                        task = task,
                        onClick = { onTaskClick(index) },
                        onCheckedChange = { newValue ->
                            onTaskCompleted(index, newValue)
                        }
                    )
                }
            }
        }
    }
}
