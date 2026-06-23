package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CrearPagoDto(
    @SerializedName("MetodoPago") val metodoPago: String,
    @SerializedName("EstadoPago") val estadoPago: String,
    @SerializedName("Monto") val monto: Double
)