package com.app.partidos.presentation.purchase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.partidos.domain.model.Pago

import com.app.partidos.presentation.validation.ValidationScreen

@Composable
fun PurchaseScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: PurchaseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var pagoParaValidar by remember { mutableStateOf<Pago?>(null) }

    if (pagoParaValidar != null && uiState.partido != null) {
        ValidationScreen(
            partidoId = uiState.partido!!.id,
            cantidad = uiState.cantidad.toInt(),
            pago = pagoParaValidar!!,
            onNavigateToHome = onNavigateToHome,
            onNavigateBack = { pagoParaValidar = null }
        )
        return
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else if (uiState.error != null && uiState.partido == null) {
            Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) { Text("Volver") }
        } else if (uiState.partido != null) {
            Text("Comprar Entradas", style = MaterialTheme.typography.headlineMedium)
            Text("Partido: ${uiState.partido!!.equipoLocal} vs ${uiState.partido!!.equipoVisitante}")
            Text("Precio unitario: \$${uiState.partido!!.precio}")

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.cantidad,
                onValueChange = { viewModel.onCantidadChanged(it) },
                label = { Text("Cantidad de Entradas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Total a pagar: \$${uiState.totalCalculado}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.numeroTarjeta,
                onValueChange = { viewModel.onTarjetaChanged(it) },
                label = { Text("Número de Tarjeta") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.nombreTitular,
                onValueChange = { viewModel.onTitularChanged(it) },
                label = { Text("Nombre del Titular") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = uiState.vencimiento,
                    onValueChange = { viewModel.onVencimientoChanged(it) },
                    label = { Text("Venc. (MM/AA)") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = uiState.cvv,
                    onValueChange = { viewModel.onCvvChanged(it) },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            if (uiState.error != null) {
                Text(uiState.error!!, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.validarYContinuar { pago ->
                        pagoParaValidar = pago
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text("Confirmar Datos y Pagar")
            }
            
            OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                Text("Cancelar")
            }
        }
    }
}
