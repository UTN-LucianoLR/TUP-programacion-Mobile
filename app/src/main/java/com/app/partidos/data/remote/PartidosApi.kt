package com.app.partidos.data.remote

import com.app.partidos.data.remote.dto.PartidoDto
import com.app.partidos.data.remote.dto.CompraResponseDto
import com.app.partidos.data.remote.dto.CrearCompraDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PartidosApi {

    @GET("api/Partido/obtenertodos")
    suspend fun getPartidos(): List<PartidoDto>

    @POST("api/Compra/crear")
    suspend fun processPayment(
        @Body request: CrearCompraDto
    ): CompraResponseDto
}
