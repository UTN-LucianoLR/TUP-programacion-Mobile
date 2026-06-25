package com.app.partidos.presentation.purchase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
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

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.LightGray,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.LightGray,
        cursorColor = Color.White
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF101F3D)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE63946))
                }
            } else if (uiState.error != null && uiState.partido == null) {
                Text("Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
                ) { Text("Volver") }
            } else if (uiState.partido != null) {
                Text(
                    "Comprar Entradas",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )
                Text(
                    "Partido: ${uiState.partido!!.equipoLocal} vs ${uiState.partido!!.equipoVisitante}",
                    color = Color.White
                )
                Text(
                    "Precio unitario: \$${uiState.partido!!.precio}",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.cantidad,
                    onValueChange = { viewModel.onCantidadChanged(it) },
                    label = { Text("Cantidad de Entradas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Total a pagar: \$${uiState.totalCalculado}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Yellow
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Selector de método de pago
                Text("Método de pago", style = MaterialTheme.typography.titleSmall, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    listOf("Tarjeta", "Transferencia").forEach { metodo ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .weight(1f)
                                .selectable(
                                    selected = uiState.metodoPago == metodo,
                                    onClick  = { viewModel.onMetodoPagoChanged(metodo) },
                                    role     = Role.RadioButton
                                )
                                .padding(vertical = 8.dp)
                        ) {
                            RadioButton(
                                selected = uiState.metodoPago == metodo,
                                onClick  = { viewModel.onMetodoPagoChanged(metodo) }
                            )
                            Text(
                                text     = metodo,
                                modifier = Modifier.padding(start = 4.dp),
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.metodoPago == "Tarjeta") {
                    OutlinedTextField(
                        value = uiState.numeroTarjeta,
                        onValueChange = { viewModel.onTarjetaChanged(it) },
                        label = { Text("Número de Tarjeta") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    OutlinedTextField(
                        value = uiState.nombreTitular,
                        onValueChange = { viewModel.onTitularChanged(it) },
                        label = { Text("Nombre del Titular") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = uiState.vencimiento,
                            onValueChange = { viewModel.onVencimientoChanged(it) },
                            label = { Text("Venc. (MM/AA)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                        OutlinedTextField(
                            value = uiState.cvv,
                            onValueChange = { viewModel.onCvvChanged(it) },
                            label = { Text("CVV") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors
                        )
                    }
                }

                if (uiState.metodoPago == "Transferencia") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text  = "Datos para la transferencia",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("CBU: 0000003100012345678901")
                            Text("Alias: MUNDIAL.TUP.2026")
                            Text("Titular: TUP Mundial S.A.")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text  = "Tu compra quedará en estado Pendiente hasta que se confirme la acreditación.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
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
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE63946))
                ) {
                    Text("Confirmar Datos y Pagar")
                }

                OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancelar", color = Color.White)
                }
            }
        }
    }
}
