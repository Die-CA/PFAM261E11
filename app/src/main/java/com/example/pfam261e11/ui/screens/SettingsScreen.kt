package com.example.pfam261e11.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import com.example.pfam261e11.notifications.NotificationScheduler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {

    val context = LocalContext.current
    var dailyEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Opciones de configuración",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("• Tema oscuro (próximamente)")
            Text("• Notificaciones (próximamente)")

            // 🔥 NUEVO: Switch notificaciones diarias
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Notificaciones diarias")
                Switch(
                    checked = dailyEnabled,
                    onCheckedChange = { enabled ->
                        dailyEnabled = enabled

                        if (enabled) {
                            NotificationScheduler.scheduleDailyNotification(
                                context = context,
                                hour = 9,
                                minute = 0,
                                title = "Recordatorio diario",
                                description = "Revisa tus tareas pendientes"
                            )
                        } else {
                            NotificationScheduler.cancelDailyNotification(context)
                        }
                    }
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    NotificationScheduler.scheduleNotification(
                        context,
                        "Prueba: ¡esta notificación funciona!"
                    )
                },

                        modifier = Modifier.fillMaxWidth()
            ) {
                Text("Probar notificación (5 segundos)")
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Regresar")
            }
        }
    }
}
