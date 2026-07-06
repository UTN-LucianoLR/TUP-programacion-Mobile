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

    fun onTarjetaChanged(t: String) { 
        val digits = t.filter { it.isDigit() }.take(16)
        _uiState.value = _uiState.value.copy(numeroTarjeta = digits) 
    }
    fun onTitularChanged(t: String) { _uiState.value = _uiState.value.copy(nombreTitular = t) }
    fun onVencimientoChanged(v: String) { 
        val digits = v.filter { it.isDigit() }.take(4)
        _uiState.value = _uiState.value.copy(vencimiento = digits) 
    }
    fun onCvvChanged(c: String) { 
        val digits = c.filter { it.isDigit() }.take(4)
        _uiState.value = _uiState.value.copy(cvv = digits) 
    }

    fun onMetodoPagoChanged(metodo: String) {
        _uiState.value = _uiState.value.copy(
            metodoPago = metodo,
            error = null
        )
    }

    fun validarYContinuar(onSuccess: (Pago) -> Unit) {
        val st   = _uiState.value
        val cant = st.cantidad.toIntOrNull() ?: 0

        // Validación de cantidad (aplica a todos los métodos)
        if (cant < 1 || cant > 10) {
            _uiState.value = st.copy(error = "Puede comprar entre 1 y 10 entradas")
            return
        }

        // Si el método es Transferencia: omitir validación de tarjeta
        if (st.metodoPago == "Transferencia") {
            val pago = Pago(
                metodoPago    = "Transferencia",
                monto         = st.totalCalculado
                // Campos de tarjeta quedan null por defecto
            )
            _uiState.value = st.copy(error = null)
            onSuccess(pago)
            return
        }

        // Validaciones Regex para Tarjeta
        val regexTarjeta     = Regex("""^\d{16}$""")
        val regexCvv         = Regex("""^\d{3,4}$""")
        val regexTitular     = Regex("""^[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+$""")
        val regexVencimiento = Regex("""^(0[1-9]|1[0-2])\d{2}$""") // Solo 4 números MMYY

        if (!regexTarjeta.matches(st.numeroTarjeta)) {
            _uiState.value = st.copy(error = "El número de tarjeta debe tener exactamente 16 dígitos.")
            return
        }
        if (!regexTitular.matches(st.nombreTitular)) {
            _uiState.value = st.copy(error = "El titular solo puede contener letras y espacios.")
            return
        }
        if (!regexVencimiento.matches(st.vencimiento) || !esVencimientoFuturo(st.vencimiento)) {
            _uiState.value = st.copy(error = "Ingresá un vencimiento válido y futuro (MMAA).")
            return
        }
        if (!regexCvv.matches(st.cvv)) {
            _uiState.value = st.copy(error = "El CVV debe tener 3 o 4 dígitos.")
            return
        }

        // Formatear el vencimiento a MM/YY para la API si tiene 4 dígitos
        val vencimientoFormateado = if (st.vencimiento.length == 4) {
            "${st.vencimiento.substring(0, 2)}/${st.vencimiento.substring(2, 4)}"
        } else {
            st.vencimiento
        }

        val pago = Pago(
            metodoPago    = "Tarjeta",
            monto         = st.totalCalculado,
            numeroTarjeta = st.numeroTarjeta,
            nombreTitular = st.nombreTitular,
            vencimiento   = vencimientoFormateado,
            cvv           = st.cvv
        )
        _uiState.value = st.copy(error = null)
        onSuccess(pago)
    }

    private fun esVencimientoFuturo(vencimiento: String): Boolean {
        return try {
            if (vencimiento.length != 4) return false
            val mes        = vencimiento.substring(0, 2).toInt()
            val anio       = 2000 + vencimiento.substring(2, 4).toInt()
            val ahora      = java.util.Calendar.getInstance()
            val anioActual = ahora.get(java.util.Calendar.YEAR)
            val mesActual  = ahora.get(java.util.Calendar.MONTH) + 1
            anio > anioActual || (anio == anioActual && mes >= mesActual)
        } catch (_: Exception) {
            false
        }
    }
}

