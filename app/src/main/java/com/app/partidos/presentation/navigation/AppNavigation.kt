package com.app.partidos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.app.partidos.presentation.detail.MatchDetailScreen
import com.app.partidos.presentation.home.HomeScreen
import com.app.partidos.presentation.login.LoginScreen
import com.app.partidos.presentation.purchase.PurchaseScreen
import com.app.partidos.presentation.recovery.RecoveryPasswordScreen
import com.app.partidos.presentation.register.RegisterScreen
import com.app.partidos.presentation.validation.ValidationScreen
import com.app.partidos.presentation.tickets.TicketsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginRoute) {
        
        // MÓDULO AUTENTICACIÓN
        composable<LoginRoute> {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(RegistroRoute) },
                onNavigateToRecovery = { navController.navigate(RecoveryRoute) },
                onNavigateToHome = { 
                    navController.navigate(ListaPartidosRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<RegistroRoute> {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(ListaPartidosRoute) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<RecoveryRoute> {
            RecoveryPasswordScreen(onNavigateBack = { navController.popBackStack() })
        }

        // MÓDULO VISUALIZACIÓN
        composable<ListaPartidosRoute> {
            HomeScreen(
                onNavigateToDetail = { id -> navController.navigate(DetallePartidoRoute(id)) },
                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(ListaPartidosRoute) { inclusive = true }
                    }
                },
                onNavigateToTickets = { navController.navigate(MisTicketsRoute) }
            )
        }

        composable<DetallePartidoRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<DetallePartidoRoute>()
            MatchDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPurchase = { id -> navController.navigate(CheckoutCompraRoute(id, 1)) },
                onNavigateToLogin = { navController.navigate(LoginRoute) }
            )
        }

        // MÓDULO COMPRA
        composable<CheckoutCompraRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CheckoutCompraRoute>()
            PurchaseScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(MisTicketsRoute) {
                        popUpTo(ListaPartidosRoute) { inclusive = false }
                    }
                }
            )
        }
        
        composable<MisTicketsRoute> {
            TicketsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
