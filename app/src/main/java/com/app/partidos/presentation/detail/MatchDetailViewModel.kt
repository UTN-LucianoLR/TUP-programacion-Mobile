package com.app.partidos.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.repository.PartidosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MatchDetailUiState {
    object Loading : MatchDetailUiState
    data class Success(val partido: Partido) : MatchDetailUiState
    data class Error(val message: String) : MatchDetailUiState
}

@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val repository: PartidosRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val partidoId: String = checkNotNull(savedStateHandle["partidoId"])

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
                _uiState.value = MatchDetailUiState.Success(partido)
            } else {
                _uiState.value = MatchDetailUiState.Error("Partido no encontrado")
            }
        }
    }
}
