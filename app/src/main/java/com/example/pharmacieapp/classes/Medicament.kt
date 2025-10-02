package com.example.pharmacieapp.classes



import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Medicament(
    val id: Long,
    val nom: String,
    val image: String?,
    val prix: Double,
    val type: String,
    val description: String?,
    val composants: String?,
    val conseils: String?,
    val categorie: String,
    val quantity: Int
) : Parcelable
