package com.app.partidos.presentation.detail

import com.app.partidos.domain.model.Partido

sealed interface MatchDetailUiState {
    object Loading : MatchDetailUiState
    data class Success(val partido: Partido, val isPastMatch: Boolean = false) : MatchDetailUiState
    data class Error(val message: String) : MatchDetailUiState
}
