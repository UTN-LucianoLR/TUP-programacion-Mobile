package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompraResponseDto(
    @SerializedName("Id") val id: Int,
    @SerializedName("Fecha") val fecha: String,
    @SerializedName("Total") val total: Double,
    @SerializedName("Estado") val estado: String,
    @SerializedName("UsuarioId") val usuarioId: Int
)
