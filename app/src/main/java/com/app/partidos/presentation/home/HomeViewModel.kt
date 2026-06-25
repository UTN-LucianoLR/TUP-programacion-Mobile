package com.app.partidos.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.repository.AuthRepository
import com.app.partidos.domain.repository.PartidosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val partidos: List<Partido>) : HomeUiState
    object Empty : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PartidosRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        cargarPartidos()
    }

    private fun cargarPartidos() {
        _uiState.value = HomeUiState.Loading
        
        viewModelScope.launch {
            val result = repository.getPartidos(forceRefresh = true)
            if (result.isFailure) {
                _uiState.value = HomeUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            } else {
                // Solo si la API responde con una lista vacía y no hay nada en la BD
                if (_uiState.value is HomeUiState.Loading) {
                    _uiState.value = HomeUiState.Empty
                }
            }
        }

        viewModelScope.launch {
            repository.getPartidosFlow().collectLatest { partidos ->
                if (partidos.isNotEmpty()) {
                    _uiState.value = HomeUiState.Success(partidos)
                } else if (_uiState.value is HomeUiState.Success) {
                    _uiState.value = HomeUiState.Empty
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
