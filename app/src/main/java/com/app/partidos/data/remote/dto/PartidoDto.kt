package com.app.partidos.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PartidoDto(
    @SerializedName("id") val id: Int,
    @SerializedName("local") val equipoLocal: String?,
    @SerializedName("visitante") val equipoVisitante: String?,
    @SerializedName("fecha") val fecha: String?,
    @SerializedName("hora") val hora: String?,
    @SerializedName("estadio") val estadioNombre: String?
)
