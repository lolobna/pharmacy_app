package com.example.pharmacieapp.classes

data class Commande (
    val idUser : Long,
    val idMedicament :  Long,
    val quantity : Int,
    val DateReservation : String
)