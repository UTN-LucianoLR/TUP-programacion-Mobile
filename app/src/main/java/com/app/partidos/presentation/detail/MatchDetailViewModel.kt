package com.app.partidos.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.repository.AuthRepository
import com.app.partidos.domain.repository.PartidosRepository
import com.app.partidos.domain.usecase.IsMatchPastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MatchDetailViewModel @Inject constructor(
    private val repository: PartidosRepository,
    private val authRepository: AuthRepository,
    private val isMatchPastUseCase: IsMatchPastUseCase,
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
                val isPast = isMatchPastUseCase(partido.fecha, partido.hora)
                _uiState.value = MatchDetailUiState.Success(partido, isPast)
            } else {
                _uiState.value = MatchDetailUiState.Error("Partido no encontrado")
            }
        }
    }
}
