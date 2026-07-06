package com.app.partidos.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToTickets: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val partidos by viewModel.partidosFlow.collectAsState(initial = emptyList())
    val searchText by viewModel.searchText.collectAsState()
    val showPastMatches by viewModel.showPastMatches.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title), color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF101F3D)
                ),
                actions = {
                    TextButton(onClick = onNavigateToTickets) {
                        Text(stringResource(R.string.home_my_tickets), color = Color.White)
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onNavigateToLogin()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = stringResource(R.string.home_logout),
                            tint = Color.White
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFF101F3D)
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.onSearchTextChange(it) },
                placeholder = { Text(stringResource(R.string.home_search_placeholder)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedPlaceholderColor = Color.LightGray,
                    unfocusedPlaceholderColor = Color.LightGray,
                    cursorColor = Color.White
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_show_past_matches),
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = showPastMatches,
                    onCheckedChange = { viewModel.onTogglePastMatches(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFE63946)
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                if (partidos.isEmpty()) {
                    Text(
                        text = stringResource(R.string.home_no_matches),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(partidos) { partido ->
                            PartidoItem(
                                partido = partido,
                                isPast = showPastMatches,
                                onClick = { onNavigateToDetail(partido.id) }
                            )
                        }
                    }
                }
            }
    }
}
}
