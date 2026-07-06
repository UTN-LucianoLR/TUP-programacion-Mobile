package com.app.partidos.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.repository.AuthRepository
import com.app.partidos.domain.repository.PartidosRepository
import com.app.partidos.domain.usecase.IsMatchPastUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PartidosRepository,
    private val authRepository: AuthRepository,
    private val isMatchPastUseCase: IsMatchPastUseCase
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _showPastMatches = MutableStateFlow(false)
    val showPastMatches: StateFlow<Boolean> = _showPastMatches.asStateFlow()

    init {
        viewModelScope.launch {
            repository.refreshPartidos()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val partidosFlow: Flow<List<Partido>> = combine(
        _searchText,
        _showPastMatches
    ) { query, showPast ->
        Pair(query, showPast)
    }.flatMapLatest { (query, showPast) ->
        if (showPast) {
            repository.getPastPartidosFlow(query)
        } else {
            repository.getUpcomingPartidosFlow(query)
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onTogglePastMatches(show: Boolean) {
        _showPastMatches.value = show
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
