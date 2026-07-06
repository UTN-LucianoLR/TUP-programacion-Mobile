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
                    val errorBody = error.response()?.errorBody()?.string() ?: ""
                    val mensaje = try {
                        val json = org.json.JSONObject(errorBody)
                        when {
                            json.has("mensaje") -> json.getString("mensaje")
                            json.has("message") -> json.getString("message")
                            json.has("errors") -> {
                                val errors = json.getJSONObject("errors")
                                val keys = errors.keys()
                                if (keys.hasNext()) {
                                    errors.getJSONArray(keys.next()).getString(0)
                                } else if (json.has("title")) {
                                    json.getString("title")
                                } else {
                                    "Error de validación en el servidor."
                                }
                            }
                            json.has("title") -> json.getString("title")
                            else -> "Error ${error.code()}: ${error.message()}"
                        }
                    } catch (_: Exception) {
                        // El body no es JSON (HTML, texto plano)
                        if (errorBody.isNotBlank() && !errorBody.trimStart().startsWith("<")) {
                            errorBody.take(200)
                        } else {
                            "Error ${error.code()} al procesar el pago. Verificá tu conexión con el servidor."
                        }
                    }
                    _uiState.value = PaymentUiState.Rejected(message = mensaje)
                } else if (error is java.io.IOException) {
                    _uiState.value = PaymentUiState.Rejected(message = "Sin conexión. Verificá tu red e intentá nuevamente.")
                } else {
                    val msg = error.message ?: "Error desconocido"
                    _uiState.value = PaymentUiState.Rejected("Pago rechazado: $msg")
                }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = PaymentUiState.Idle
    }
}
