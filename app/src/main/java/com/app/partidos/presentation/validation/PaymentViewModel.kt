package com.app.partidos.presentation.validation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Compra
import com.app.partidos.domain.model.Pago
import com.app.partidos.domain.repository.AuthRepository
import com.app.partidos.domain.repository.PartidosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PaymentUiState {
    object Idle : PaymentUiState
    object Processing : PaymentUiState
    data class Approved(val compra: Compra) : PaymentUiState
    data class Rejected(val message: String) : PaymentUiState
}

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val partidosRepository: PartidosRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun procesarPago(partidoId: String, cantidad: Int, pago: Pago) {
        if (_uiState.value is PaymentUiState.Processing) return
        
        _uiState.value = PaymentUiState.Processing
        
        viewModelScope.launch {
            val usuario = authRepository.getUsuarioLogueado()
            if (usuario == null) {
                _uiState.value = PaymentUiState.Rejected("Usuario no autenticado")
                return@launch
            }

            val result = partidosRepository.procesarCompra(
                pago = pago,
                usuarioId = usuario.id,
                partidoId = partidoId,
                cantidad = cantidad
            )

            result.onSuccess { compra ->
                _uiState.value = PaymentUiState.Approved(compra)
            }.onFailure { error ->
                if (error is retrofit2.HttpException) {
                    val errorBody = error.response()?.errorBody()?.string()
                    val mensaje = try {
                        org.json.JSONObject(errorBody ?: "{}").getString("mensaje")
                    } catch (_: Exception) {
                        "Error al procesar el pago."
                    }
                    _uiState.value = PaymentUiState.Rejected(message = mensaje)
                } else if (error is java.io.IOException) {
                    _uiState.value = PaymentUiState.Rejected(message = "Sin conexión. Verificá tu red e intentá nuevamente.")
                } else {
                    _uiState.value = PaymentUiState.Rejected(error.message ?: "Pago rechazado")
                }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = PaymentUiState.Idle
    }
}
