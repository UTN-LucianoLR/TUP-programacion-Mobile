package com.app.partidos.presentation.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecoveryUiState())
    val uiState: StateFlow<RecoveryUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) { _uiState.value = _uiState.value.copy(email = email) }

    fun onNombreChanged(nombre: String) { _uiState.value = _uiState.value.copy(nombre = nombre) }
    fun onApellidoChanged(apellido: String) { _uiState.value = _uiState.value.copy(apellido = apellido) }
    fun onNuevaPasswordChanged(password: String) { _uiState.value = _uiState.value.copy(nuevaPassword = password) }
    fun onConfirmarPasswordChanged(password: String) { _uiState.value = _uiState.value.copy(confirmarPassword = password) }

    fun recuperarPassword() {
        val state = _uiState.value
        if (state.email.isBlank() || state.nombre.isBlank() || state.apellido.isBlank() || state.nuevaPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Todos los campos son obligatorios")
            return
        }
        if (state.nuevaPassword != state.confirmarPassword) {
            _uiState.value = _uiState.value.copy(error = "Las contraseñas no coinciden")
            return
        }
        if (state.nuevaPassword.length < 6) {
            _uiState.value = _uiState.value.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        
        viewModelScope.launch {
            val result = authRepository.recuperarPassword(
                email = state.email,
                nombre = state.nombre,
                apellido = state.apellido,
                nuevaPassword = state.nuevaPassword
            )
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = it.message)
            }
        }
    }
}

