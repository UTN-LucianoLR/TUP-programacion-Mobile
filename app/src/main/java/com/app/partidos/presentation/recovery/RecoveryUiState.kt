package com.app.partidos.presentation.recovery

data class RecoveryUiState(
    val email: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val nuevaPassword: String = "",
    val confirmarPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
