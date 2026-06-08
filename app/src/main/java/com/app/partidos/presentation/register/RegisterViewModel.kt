package com.app.partidos.presentation.register

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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNombreChanged(nombre: String) { _uiState.value = _uiState.value.copy(nombre = nombre) }
    fun onEmailChanged(email: String) { _uiState.value = _uiState.value.copy(email = email) }
    fun onPasswordChanged(password: String) { _uiState.value = _uiState.value.copy(password = password) }

    fun registrar() {
        val st = _uiState.value
        if (st.nombre.isBlank() || st.email.isBlank() || st.password.isBlank()) {
            _uiState.value = st.copy(error = "Complete todos los campos")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(st.email).matches()) {
            _uiState.value = st.copy(error = "El email no tiene un formato válido")
            return
        }
        if (st.password.length < 6) {
            _uiState.value = st.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }
        _uiState.value = st.copy(isLoading = true, error = null)
        viewModelScope.launch {
            // Nota: Pasamos Strings vacíos para apellido y telefono ya que no existen en el nuevo dominio
            val result = authRepository.registrarUsuario(st.nombre, "", st.email, st.password, "")
            result.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, error = it.message)
            }
        }
    }
}

data class RegisterUiState(
    val nombre: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
