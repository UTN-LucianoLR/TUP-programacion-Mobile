package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CrearCompraDto(
    @SerializedName("UsuarioId") val usuarioId: Int,
    @SerializedName("Pago") val pago: CrearPagoDto,
    @SerializedName("Tickets") val tickets: List<CrearTicketDto>
)




