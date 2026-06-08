package com.app.partidos.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partidos.domain.model.Partido

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Partidos del Mundial") },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        onNavigateToLogin()
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Spacer(modifier = Modifier.height(8.dp))

        when (val state = uiState) {
            is HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay partidos disponibles.")
                }
            }
            is HomeUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                }
            }
            is HomeUiState.Success -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.partidos) { partido ->
                        PartidoItem(partido = partido, onClick = { onNavigateToDetail(partido.id) })
                    }
                }
            }
        }
    }
}
}

@Composable
fun PartidoItem(partido: Partido, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${partido.equipoLocal} vs ${partido.equipoVisitante}", style = MaterialTheme.typography.titleLarge)
            Text("Estadio: ${partido.estadio}")
            Text("Fecha: ${partido.fecha} - ${partido.hora}")
            if (partido.disponible) {
                Text("Entradas disponibles: ${partido.entradasDisponibles}", color = MaterialTheme.colorScheme.primary)
            } else {
                Text("Entradas Agotadas", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
