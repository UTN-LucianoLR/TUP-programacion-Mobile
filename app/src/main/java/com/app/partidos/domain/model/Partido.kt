package com.app.partidos.domain.model

data class Partido(
    val id: String,
    val equipoLocal: String,
    val codigoLocal: String,
    val equipoVisitante: String,
    val codigoVisitante: String,
    val fecha: String,
    val hora: String,
    val estadio: String,
    val precio: Double,
    val entradasDisponibles: Int
) {
    val disponible: Boolean
        get() = entradasDisponibles > 0
}
