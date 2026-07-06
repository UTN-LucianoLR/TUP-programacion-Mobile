package com.app.partidos.presentation.tickets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.app.partidos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsScreen(
    onNavigateBack: () -> Unit,
    viewModel: TicketsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.tickets_title), color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101F3D),
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button), tint = Color.White)
                    }
                }
            )
        },
        containerColor = Color(0xFF101F3D)
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (val state = uiState) {
                is TicketsUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFE63946)
                    )
                }
                is TicketsUiState.Error -> {
                    Text(stringResource(R.string.error_message, state.message), color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                }
                is TicketsUiState.Success -> {
                    if (state.tickets.isEmpty()) {
                        Text(stringResource(R.string.tickets_empty), color = Color.White, modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.tickets) { compra ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2D4A))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        if (compra.equipoLocal.isNotEmpty() && compra.equipoVisitante.isNotEmpty()) {
                                            Text(stringResource(R.string.tickets_match_title, compra.equipoLocal, compra.equipoVisitante), style = MaterialTheme.typography.titleMedium, color = Color.White)
                                        } else {
                                            Text(stringResource(R.string.tickets_match_id, compra.partidoId), style = MaterialTheme.typography.titleMedium, color = Color.White)
                                        }
                                        if (compra.fechaPartido.isNotEmpty()) {
                                            Text(stringResource(R.string.tickets_match_date, compra.fechaPartido), style = MaterialTheme.typography.bodyMedium, color = Color.White)
                                        }
                                        Text(stringResource(R.string.tickets_quantity, compra.cantidadEntradas), color = Color.LightGray)
                                        Text(stringResource(R.string.tickets_total, compra.total.toString()), color = Color(0xFF4CAF50))
                                        Text(stringResource(R.string.tickets_date, compra.fechaCompra), style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
