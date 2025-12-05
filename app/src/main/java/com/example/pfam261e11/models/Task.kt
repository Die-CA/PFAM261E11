package com.example.pfam261e11.models

data class Task(
    val title: String,
    val description: String,
    val hour: Int? = null,
    val minute: Int? = null,
    val imageUri: String? = null,
    val isCompleted: Boolean = false,

)
