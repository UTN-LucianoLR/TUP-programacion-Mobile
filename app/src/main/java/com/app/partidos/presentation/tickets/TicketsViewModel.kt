package com.app.partidos.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Compra
import com.app.partidos.domain.repository.AuthRepository
import com.app.partidos.domain.repository.PartidosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TicketsViewModel @Inject constructor(
    private val partidosRepository: PartidosRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TicketsUiState>(TicketsUiState.Loading)
    val uiState: StateFlow<TicketsUiState> = _uiState.asStateFlow()

    init {
        cargarTickets()
    }

    private fun cargarTickets() {
        viewModelScope.launch {
            val user = authRepository.getUsuarioLogueado()
            if (user == null) {
                _uiState.value = TicketsUiState.Error("No hay sesión activa")
                return@launch
            }
            
            partidosRepository.getHistorialComprasFlow(user.id).collectLatest { compras ->
                _uiState.value = TicketsUiState.Success(compras)
            }
        }
    }
}
