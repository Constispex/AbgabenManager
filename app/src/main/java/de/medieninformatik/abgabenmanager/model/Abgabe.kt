package de.medieninformatik.abgabenmanager.model

/**
 * Die Klasse beinhaltet die Daten einer Abgabe
 */
data class Abgabe(
    val id: Int, val name: String, val date: String, val description: String, var isDone: Boolean
)