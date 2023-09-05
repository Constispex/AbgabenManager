package de.medieninformatik.abgabenmanager.model

data class Abgabe(
    val id: Int,
    val name: String,
    val date: String,
    val description: String,
    val isDone: Boolean
)