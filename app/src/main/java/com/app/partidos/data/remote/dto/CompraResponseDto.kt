package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CompraResponseDto(
    @SerializedName(value = "id", alternate = ["Id"]) val id: Int,
    @SerializedName(value = "fecha", alternate = ["Fecha"]) val fecha: String,
    @SerializedName(value = "total", alternate = ["Total"]) val total: Double,
    @SerializedName(value = "estado", alternate = ["Estado"]) val estado: String,
    @SerializedName(value = "usuarioId", alternate = ["UsuarioId"]) val usuarioId: Int
)