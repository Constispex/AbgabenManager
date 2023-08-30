package de.medieninformatik.abgabenmanager.model

data class Abgabe(
    val id: Int,
    val name: String,
    val date: String,
    val subject: String,
    val tasks: List<Task>,
    val description: String,
    val isDone: Boolean
)