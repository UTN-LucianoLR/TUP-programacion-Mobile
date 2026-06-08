package com.app.partidos.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder

@Composable
fun MatchDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPurchase: (String) -> Unit,
    viewModel: MatchDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val imageLoader = androidx.compose.runtime.remember {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        when (val state = uiState) {
            is MatchDetailUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MatchDetailUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
                            Text("Volver al Listado")
                        }
                    }
                }
            }
            is MatchDetailUiState.Success -> {
                val partido = state.partido
                Text("Detalles del Partido", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    AsyncImage(
                        model = "file:///android_asset/flags/${partido.codigoLocal}.svg",
                        contentDescription = "Bandera ${partido.equipoLocal}",
                        imageLoader = imageLoader,
                        modifier = Modifier.size(40.dp).padding(end = 8.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        text = "${partido.equipoLocal} vs ${partido.equipoVisitante}",
                        style = MaterialTheme.typography.titleLarge
                    )
                    AsyncImage(
                        model = "file:///android_asset/flags/${partido.codigoVisitante}.svg",
                        contentDescription = "Bandera ${partido.equipoVisitante}",
                        imageLoader = imageLoader,
                        modifier = Modifier.size(40.dp).padding(start = 8.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                
                Text("Estadio: ${partido.estadio}")
                Text("Fecha: ${partido.fecha}")
                Text("Hora: ${partido.hora}")
                Text("Precio unitario: \$${partido.precio}")
                Text(
                    text = if (partido.disponible) "Entradas disponibles: ${partido.entradasDisponibles}" else "Agotado",
                    color = if (partido.disponible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onNavigateToPurchase(partido.id) },
                    enabled = partido.disponible,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Text("Comprar Entradas")
                }
                
                OutlinedButton(onClick = onNavigateBack, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver")
                }
            }
        }
    }
}
