package com.example.pfam261e11.ui.screens

import android.app.TimePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.util.*

@Composable
fun AddTaskScreen(
    onSave: (String, String, Int, Int, String?) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedHour by remember { mutableStateOf(12) }
    var selectedMinute by remember { mutableStateOf(0) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    //   GALERIA
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) imageUri = uri
    }

    //   CÁMARA: archivo temporal
    val photoFile = remember {
        File(context.externalCacheDir, "captured_${UUID.randomUUID()}.jpg")
    }

    val photoUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
    }

    //   CÁMARA
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) imageUri = photoUri
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) takePictureLauncher.launch(photoUri)
    }

    //   TIME PICKER
    val timePicker = remember {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
            },
            selectedHour,
            selectedMinute,
            true
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Text("Nueva tarea", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        //                   IMAGEN
        Text("Imagen opcional", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.weight(1f)
            ) {
                Text("Elegir foto")
            }

            Button(
                onClick = {
                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Tomar foto")
            }
        }

        Spacer(Modifier.height(10.dp))

        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

//              Selector de hora
        Text("Hora de notificación", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = String.format("%02d:%02d", selectedHour, selectedMinute),
            onValueChange = {},
            label = { Text("Hora (HH:mm)") },
            readOnly = true,        // 👈 Permite mostrar texto pero no editar
            modifier = Modifier
                .width(160.dp)
                .clickable {         // 👈 El campo completo abre el TimePicker
                    timePicker.show()
                }
        )

        Spacer(Modifier.height(25.dp))

        //                    GUARDAR
        Button(
            onClick = {
                if (title.isNotEmpty()) {
                    onSave(
                        title,
                        description,
                        selectedHour,
                        selectedMinute,
                        imageUri?.toString()
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

        TextButton(
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }
    }
}
