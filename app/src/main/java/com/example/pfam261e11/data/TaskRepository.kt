package com.example.pfam261e11.data

import android.content.Context
import com.example.pfam261e11.models.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class TaskRepository(private val context: Context) {

    private val fileName = "tasks.json"
    private val gson = Gson()

    private fun getFile(): File {
        return File(context.filesDir, fileName)
    }

    fun loadTasks(): List<Task> {
        val file = getFile()

        if (!file.exists()) return emptyList()

        val json = file.readText()

        val type = object : TypeToken<List<Task>>() {}.type

        return gson.fromJson(json, type)
    }

    fun saveTasks(tasks: List<Task>) {
        val file = getFile()
        val json = gson.toJson(tasks)
        file.writeText(json)
    }
}
