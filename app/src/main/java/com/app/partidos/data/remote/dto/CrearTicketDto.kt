package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CrearTicketDto(
    @SerializedName("PartidoId") val partidoId: Int,
    @SerializedName("Sector") val sector: String,
    @SerializedName("Fila") val fila: String,
    @SerializedName("Asiento") val asiento: String,
    @SerializedName("Precio") val precio: Double
)