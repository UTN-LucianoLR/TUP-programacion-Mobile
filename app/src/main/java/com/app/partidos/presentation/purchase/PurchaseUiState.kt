package com.app.partidos.presentation.purchase

import com.app.partidos.domain.model.Partido

data class PurchaseUiState(
    val partido: Partido? = null,
    val cantidad: String = "1",
    val metodoPago: String = "Tarjeta",
    val numeroTarjeta: String = "",
    val nombreTitular: String = "",
    val vencimiento: String = "",
    val cvv: String = "",
    val totalCalculado: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
