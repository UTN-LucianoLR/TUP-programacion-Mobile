package com.app.partidos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.partidos.domain.model.Pago
import com.app.partidos.presentation.detail.MatchDetailScreen
import com.app.partidos.presentation.home.HomeScreen
import com.app.partidos.presentation.login.LoginScreen
import com.app.partidos.presentation.purchase.PurchaseScreen
import com.app.partidos.presentation.recovery.RecoveryPasswordScreen
import com.app.partidos.presentation.register.RegisterScreen
import com.app.partidos.presentation.validation.ValidationScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        
        // MÓDULO AUTENTICACIÓN
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToRecovery = { navController.navigate("recovery") },
                onNavigateToHome = { 
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("recovery") {
            RecoveryPasswordScreen(onNavigateBack = { navController.popBackStack() })
        }

        // MÓDULO VISUALIZACIÓN
        composable("home") {
            HomeScreen(
                onNavigateToDetail = { id -> navController.navigate("detail/$id") },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "detail/{partidoId}",
            arguments = listOf(navArgument("partidoId") { type = NavType.StringType })
        ) {
            MatchDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPurchase = { id -> navController.navigate("purchase/$id") }
            )
        }

        // MÓDULO COMPRA
        composable(
            route = "purchase/{partidoId}",
            arguments = listOf(navArgument("partidoId") { type = NavType.StringType })
        ) {
            PurchaseScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToValidation = { partidoId, cantidad, pago ->
                    val encodedTitular = URLEncoder.encode(pago.nombreTitular, StandardCharsets.UTF_8.toString())
                    val encodedVenc = URLEncoder.encode(pago.vencimiento, StandardCharsets.UTF_8.toString())
                    navController.navigate("validation/$partidoId/$cantidad/${pago.numeroTarjeta}/$encodedTitular/$encodedVenc/${pago.cvv}/${pago.monto}")
                }
            )
        }

        // MÓDULO VALIDACIÓN Y RESULTADOS
        composable(
            route = "validation/{partidoId}/{cantidad}/{tarjeta}/{titular}/{venc}/{cvv}/{monto}",
            arguments = listOf(
                navArgument("partidoId") { type = NavType.StringType },
                navArgument("cantidad") { type = NavType.IntType },
                navArgument("tarjeta") { type = NavType.StringType },
                navArgument("titular") { type = NavType.StringType },
                navArgument("venc") { type = NavType.StringType },
                navArgument("cvv") { type = NavType.StringType },
                navArgument("monto") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val partidoId = args.getString("partidoId")!!
            val cantidad = args.getInt("cantidad")
            val tarjeta = args.getString("tarjeta")!!
            val titular = URLDecoder.decode(args.getString("titular")!!, StandardCharsets.UTF_8.toString())
            val venc = URLDecoder.decode(args.getString("venc")!!, StandardCharsets.UTF_8.toString())
            val cvv = args.getString("cvv")!!
            val monto = args.getFloat("monto").toDouble()

            val pago = Pago(tarjeta, titular, venc, cvv, monto)

            ValidationScreen(
                partidoId = partidoId,
                cantidad = cantidad,
                pago = pago,
                onNavigateToHome = {
                    navController.navigate("home") {
                        // Vuelve al home destruyendo TODO el historial de la compra
                        popUpTo("home") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() } // Regreso destructivo a Purchase
            )
        }
    }
}
