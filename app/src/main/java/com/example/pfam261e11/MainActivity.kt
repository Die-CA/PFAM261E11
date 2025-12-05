package com.example.pfam261e11

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pfam261e11.ui.screens.HomeScreen
import com.example.pfam261e11.ui.screens.AddTaskScreen
import com.example.pfam261e11.viewmodels.TodoViewModel
import com.example.pfam261e11.viewmodels.TodoViewModelFactory
import com.example.pfam261e11.ui.theme.PFAM261E11Theme
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.pfam261e11.notifications.NotificationHelper
import com.example.pfam261e11.ui.screens.TaskDetailScreen
import com.example.pfam261e11.ui.screens.SettingsScreen
import com.example.pfam261e11.ui.screens.AboutScreen
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pfam261e11.notifications.NotificationScheduler


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { /* opcional */ }

    private val viewModel: TodoViewModel by viewModels {
        TodoViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }

        if (checkSelfPermission(android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }

        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)


        setContent {
            PFAM261E11Theme {
                AppNavigation(viewModel)
            }
        }
    }
}


@Composable
fun AppNavigation(viewModel: TodoViewModel) {
    val navController = rememberNavController()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // HOME
        composable("home") {
            HomeScreen(
                tasks = tasks,
                onAddClick = { navController.navigate("addTask") },
                onTaskClick = { index -> navController.navigate("details/$index") },
                onSettingsClick = { navController.navigate("settings") },
                onAboutClick = { navController.navigate("about") },
                onTaskCompleted = { index, completed ->
                    viewModel.toggleTaskCompleted(index, completed)
                }
            )
        }

        // ADD TASK
        composable("addTask") {
            val context = androidx.compose.ui.platform.LocalContext.current

            AddTaskScreen(
                onSave = { title, desc, hour, minute, imageUri ->

                    viewModel.addTask(title, desc, hour, minute, imageUri)

                    NotificationScheduler.scheduleDailyNotification(
                        context = context,
                        hour = hour,
                        minute = minute,
                        title = title,
                        description = desc
                    )

                    navController.popBackStack()
                },

                onCancel = {
                    navController.popBackStack()
                }
            )
        }


        // TASK DETAILS
        composable("details/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toInt()

            if (index == null) {
                navController.popBackStack()
                return@composable
            }

            val currentTasks = tasks
            if (index !in currentTasks.indices) {
                if (navController.currentDestination?.route?.startsWith("details") == true) {
                    navController.popBackStack()
                }
                return@composable
            }

            val task = currentTasks[index]

            TaskDetailScreen(
                task = task,
                onBack = { navController.popBackStack() },
                onDelete = {
                    navController.popBackStack()
                    viewModel.deleteTask(index)
                }
            )
        }

        // SETTINGS
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ABOUT
        composable("about") {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

//AGREGAR UBICACION, MEJORAR ASPECTO