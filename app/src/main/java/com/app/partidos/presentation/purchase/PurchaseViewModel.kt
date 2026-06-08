package com.app.partidos.presentation.purchase

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Pago
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.repository.PartidosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val repository: PartidosRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val partidoId: String = checkNotNull(savedStateHandle["partidoId"])

    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val partido = repository.getPartidoById(partidoId)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                partido = partido,
                // Calculamos el total base con cantidad = 1
                totalCalculado = partido?.precio ?: 0.0,
                error = if (partido == null) "Partido no encontrado" else null
            )
        }
    }

    fun onCantidadChanged(cant: String) {
        val cantidad = cant.toIntOrNull() ?: 0
        val precio = _uiState.value.partido?.precio ?: 0.0
        _uiState.value = _uiState.value.copy(
            cantidad = cant,
            totalCalculado = cantidad * precio
        )
    }

    fun onTarjetaChanged(t: String) { _uiState.value = _uiState.value.copy(numeroTarjeta = t) }
    fun onTitularChanged(t: String) { _uiState.value = _uiState.value.copy(nombreTitular = t) }
    fun onVencimientoChanged(v: String) { _uiState.value = _uiState.value.copy(vencimiento = v) }
    fun onCvvChanged(c: String) { _uiState.value = _uiState.value.copy(cvv = c) }

    fun validarYContinuar(onSuccess: (Pago) -> Unit) {
        val st = _uiState.value
        val cant = st.cantidad.toIntOrNull() ?: 0
        
        if (cant < 1 || cant > 10) {
            _uiState.value = st.copy(error = "Puede comprar entre 1 y 10 entradas")
            return
        }
        if (st.numeroTarjeta.isBlank() || st.nombreTitular.isBlank() || st.vencimiento.isBlank() || st.cvv.isBlank()) {
            _uiState.value = st.copy(error = "Complete los datos bancarios")
            return
        }
        
        val pago = Pago(
            numeroTarjeta = st.numeroTarjeta,
            nombreTitular = st.nombreTitular,
            vencimiento = st.vencimiento,
            cvv = st.cvv,
            monto = st.totalCalculado
        )
        onSuccess(pago)
    }
}

data class PurchaseUiState(
    val partido: Partido? = null,
    val cantidad: String = "1",
    val numeroTarjeta: String = "",
    val nombreTitular: String = "",
    val vencimiento: String = "",
    val cvv: String = "",
    val totalCalculado: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)
