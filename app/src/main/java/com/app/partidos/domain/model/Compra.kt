package com.app.partidos.domain.model

data class Compra(
    val id: String,
    val usuarioId: String,
    val partidoId: String,
    val cantidadEntradas: Int,
    val total: Double,
    val fechaCompra: String
)
