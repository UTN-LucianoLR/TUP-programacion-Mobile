package com.app.partidos.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.repository.AuthRepository
import com.app.partidos.domain.repository.PartidosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MatchDetailUiState {
    object Loading : MatchDetailUiState
    data class Success(val partido: Partido, val isPastMatch: Boolean = false) : MatchDetailUiState
    data class Error(val message: String) : MatchDetailUiState
}

@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val repository: PartidosRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val partidoId: String = checkNotNull(savedStateHandle["partidoId"])

    val isLoggedIn: StateFlow<Boolean> = authRepository.getUsuarioLogueadoFlow()
        .map { it != null }
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), false)

    private val _uiState = MutableStateFlow<MatchDetailUiState>(MatchDetailUiState.Loading)
    val uiState: StateFlow<MatchDetailUiState> = _uiState.asStateFlow()

    init {
        cargarDetalle()
    }

    private fun cargarDetalle() {
        _uiState.value = MatchDetailUiState.Loading
        viewModelScope.launch {
            val partido = repository.getPartidoById(partidoId)
            if (partido != null) {
                var isPast = false
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
                    
                    val now = java.util.Calendar.getInstance()
                    isPast = cal.timeInMillis <= now.timeInMillis
                } catch (e: Exception) {
                    isPast = false
                }
                _uiState.value = MatchDetailUiState.Success(partido, isPast)
            } else {
                _uiState.value = MatchDetailUiState.Error("Partido no encontrado")
            }
        }
    }
}
