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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
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

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _showPastMatches = MutableStateFlow(false)
    val showPastMatches: StateFlow<Boolean> = _showPastMatches.asStateFlow()

    init {
        cargarPartidos()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onTogglePastMatches(show: Boolean) {
        _showPastMatches.value = show
    }

    private fun cargarPartidos() {
        _uiState.value = HomeUiState.Loading
        
        viewModelScope.launch {
            val result = repository.getPartidos(forceRefresh = true)
            if (result.isFailure) {
                _uiState.value = HomeUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            } else {
                if (_uiState.value is HomeUiState.Loading) {
                    _uiState.value = HomeUiState.Empty
                }
            }
        }

        viewModelScope.launch {
            try {
                combine(
                    repository.getPartidosFlow(),
                    _searchText,
                    _showPastMatches
                ) { partidosBD, query, showPast ->
                    val now = java.util.Calendar.getInstance()

                    val partidosFiltradosPorFecha = partidosBD.filter { partido ->
                        try {
                            val fechaParts = partido.fecha.take(10).split("-")
                            val year = fechaParts[0].toInt()
                            val month = fechaParts[1].toInt() - 1
                            val day = fechaParts[2].toInt()

                            val timeParts = partido.hora.split(":")
                            val hour = timeParts[0].toInt()
                            val minute = timeParts[1].toInt()

                            val cal = java.util.Calendar.getInstance()
                            cal.set(year, month, day, hour, minute, 0)

                            if (showPast) {
                                cal.timeInMillis <= now.timeInMillis
                            } else {
                                cal.timeInMillis > now.timeInMillis
                            }
                        } catch (e: Exception) {
                            !showPast // Ante duda de parseo, lo mostramos como futuro por defecto
                        }
                    }

                    val filtrados = partidosFiltradosPorFecha.filter {
                        it.equipoLocal.contains(query, ignoreCase = true) ||
                        it.equipoVisitante.contains(query, ignoreCase = true) ||
                        it.estadio.contains(query, ignoreCase = true) ||
                        it.fecha.contains(query, ignoreCase = true)
                    }

                    filtrados
                }.collectLatest { filtrados ->
                    if (_uiState.value !is HomeUiState.Error) {
                        if (filtrados.isNotEmpty()) {
                            _uiState.value = HomeUiState.Success(filtrados)
                        } else {
                            _uiState.value = HomeUiState.Empty
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error al cargar partidos: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
