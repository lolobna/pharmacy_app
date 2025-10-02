package com.example.pharmacieapp.classes


data class Ordonnance(
    val id: Long = 0,
    val userId: Long,
    val ordonnanceImage: String,
    val dateOrdonnance: String
)
