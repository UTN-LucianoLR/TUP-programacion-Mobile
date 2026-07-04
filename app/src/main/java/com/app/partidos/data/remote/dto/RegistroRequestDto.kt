package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegistroRequestDto(
    @SerializedName("Nombre") val nombre: String,
    @SerializedName("Apellido") val apellido: String,
    @SerializedName("Email") val email: String,
    @SerializedName("Password") val password: String,
    @SerializedName("ConfirmPassword") val confirmarPassword: String
)
