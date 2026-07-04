package com.app.partidos.domain.model

data class Pago(
    val metodoPago:    String,
    val monto:         Double,
    // Campos de tarjeta — null cuando metodoPago == "Transferencia"
    val numeroTarjeta: String? = null,
    val nombreTitular: String? = null,
    val vencimiento:   String? = null,
    val cvv:           String? = null
)
