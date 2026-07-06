package com.app.partidos.presentation.validation

import com.app.partidos.domain.model.Compra

sealed interface PaymentUiState {
    object Idle : PaymentUiState
    object Processing : PaymentUiState
    data class Approved(val compra: Compra) : PaymentUiState
    data class Rejected(val message: String) : PaymentUiState
}
