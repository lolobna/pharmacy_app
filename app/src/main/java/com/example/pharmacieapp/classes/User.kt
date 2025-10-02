package com.example.pharmacieapp.classes



data class User(
    val id: Long,
    val nom: String,
    val prenom: String,
    val email: String,
    val password: String,
    val numero: String,
    val dateInscription: String
)
