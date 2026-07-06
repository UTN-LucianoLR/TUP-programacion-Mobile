package com.app.partidos.presentation.tickets

import com.app.partidos.domain.model.Compra

sealed interface TicketsUiState {
    object Loading : TicketsUiState
    data class Success(val tickets: List<Compra>) : TicketsUiState
    data class Error(val message: String) : TicketsUiState
}
