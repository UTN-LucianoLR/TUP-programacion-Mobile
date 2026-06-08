package com.app.partidos.domain.model

data class Pago(
    val numeroTarjeta: String,
    val nombreTitular: String,
    val vencimiento: String,
    val cvv: String,
    val monto: Double
)
