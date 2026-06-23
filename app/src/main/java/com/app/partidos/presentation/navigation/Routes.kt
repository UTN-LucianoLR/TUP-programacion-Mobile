package com.app.partidos.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ListaPartidosRoute

@Serializable
data class DetallePartidoRoute(val partidoId: String)

@Serializable
object LoginRoute

@Serializable
object RegistroRoute

@Serializable
object RecoveryRoute

@Serializable
data class CheckoutCompraRoute(
    val partidoId: String,
    val cantidadEntradas: Int
)

@Serializable
object MisTicketsRoute
