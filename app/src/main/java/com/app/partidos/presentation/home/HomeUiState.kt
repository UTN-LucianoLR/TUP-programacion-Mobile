package com.app.partidos.presentation.home

import com.app.partidos.domain.model.Partido

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val partidos: List<Partido>) : HomeUiState
    object Empty : HomeUiState
    data class Error(val message: String) : HomeUiState
}
