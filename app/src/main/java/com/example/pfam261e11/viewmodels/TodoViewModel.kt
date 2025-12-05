package com.example.pfam261e11.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pfam261e11.data.TaskRepository
import com.example.pfam261e11.models.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TaskRepository(application)

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasksFromFile()
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        val context = getApplication<Application>().applicationContext
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null

        val fileName = "img_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        return file.absolutePath
    }


    private fun loadTasksFromFile() {
        viewModelScope.launch {
            _tasks.value = repository.loadTasks()
        }
    }

    fun addTask(
        title: String,
        description: String,
        hour: Int,
        minute: Int,
        imageUri: String?
    ) {
        viewModelScope.launch {

            val finalImagePath =
                if (imageUri != null) {
                    saveImageToInternalStorage(Uri.parse(imageUri))
                } else null

            val newTask = Task(
                title = title,
                description = description,
                hour = hour,
                minute = minute,
                imageUri = finalImagePath
            )

            val updatedList = _tasks.value.toMutableList()
            updatedList.add(newTask)

            _tasks.value = updatedList
            repository.saveTasks(updatedList)
        }
    }


    fun deleteTask(index: Int) {
        viewModelScope.launch {
            val currentList = _tasks.value.toMutableList()

            if (index in currentList.indices) {
                currentList.removeAt(index)
                _tasks.value = currentList
                repository.saveTasks(currentList)
            }
        }
    }

    fun toggleTaskCompleted(index: Int, completed: Boolean) {
        viewModelScope.launch {
            val currentList = _tasks.value.toMutableList()

            if (index in currentList.indices) {
                val task = currentList[index]
                currentList[index] = task.copy(isCompleted = completed)

                _tasks.value = currentList
                repository.saveTasks(currentList)
            }
        }
    }
}
