package com.app.partidos.presentation.validation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partidos.domain.model.Pago
import androidx.compose.ui.res.stringResource
import com.app.partidos.R

@Composable
fun ValidationScreen(
    partidoId: String,
    cantidad: Int,
    pago: Pago,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Lanzamos la petición automáticamente al entrar a la pantalla por primera vez
    LaunchedEffect(Unit) {
        viewModel.procesarPago(partidoId, cantidad, pago)
    }

    when (val state = uiState) {
        is PaymentUiState.Idle, is PaymentUiState.Processing -> {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF101F3D)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFFE63946))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            stringResource(R.string.validation_processing),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                    }
                }
            }
        }
        is PaymentUiState.Approved -> {
            // Muestra la UI de éxito que creamos en el paso anterior
            SuccessScreen(compra = state.compra, onNavigateToHome = onNavigateToHome)
        }
        is PaymentUiState.Rejected -> {
            // Muestra la UI de error que creamos en el paso anterior
            PaymentErrorScreen(
                errorMessage = state.message,
                onRetry = { viewModel.procesarPago(partidoId, cantidad, pago) },
                onCancel = onNavigateBack
            )
        }
    }
}
