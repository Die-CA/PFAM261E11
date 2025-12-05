package com.example.pfam261e11.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import com.example.pfam261e11.models.Task

class TaskViewModels : ViewModel() {

    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    fun addTask(
        title: String,
        description: String,
        hour: Int,
        minute: Int,
        imageUri: String?
    ) {
        _tasks.add(
            Task(
                title = title,
                description = description,
                hour = hour,
                minute = minute,
                imageUri = imageUri
            )
        )
    }

    fun getTask(index: Int): Task = _tasks[index]
}
