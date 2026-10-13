package com.example.parcialkotlinapp.models

data class Usuario(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val fechaNacimiento: String?,
    // Calculada por la API a partir de la fecha de nacimiento
    val edad: Int,
    val universidad: String?,
    val semestre: Int,
    val activo: Boolean,
    val fechaCreacion: String
)