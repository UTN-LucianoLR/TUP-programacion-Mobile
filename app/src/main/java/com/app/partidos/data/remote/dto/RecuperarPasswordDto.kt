package com.app.partidos.data.remote.dto

data class RecuperarPasswordDto(
    val email: String,
    val nombre: String,
    val apellido: String,
    val nuevaPassword: String
)
