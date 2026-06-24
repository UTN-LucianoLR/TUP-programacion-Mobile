package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CrearPagoDto(
    @SerializedName("MetodoPago") val metodoPago: String,
    @SerializedName("Monto") val monto: Double,
    @SerializedName("NumeroTarjeta") val numeroTarjeta: String? = null,
    @SerializedName("Titular") val titular: String? = null,
    @SerializedName("Vencimiento") val vencimiento: String? = null,
    @SerializedName("Cvv") val cvv: String? = null
)