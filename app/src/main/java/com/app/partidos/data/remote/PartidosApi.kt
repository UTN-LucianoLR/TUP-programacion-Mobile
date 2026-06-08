package com.app.partidos.data.remote

import com.app.partidos.data.remote.dto.MatchResponseDto
import com.app.partidos.data.remote.dto.PaymentRequestDto
import com.app.partidos.data.remote.dto.PaymentResponseDto
import com.app.partidos.data.remote.dto.StadiumResponseDto
import com.app.partidos.data.remote.dto.TeamResponseDto
import com.app.partidos.data.remote.dto.TournamentResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interfaz de Retrofit para interactuar con la API externa del Mundial 2026.
 */
interface PartidosApi {

    @GET("matches")
    suspend fun getMatches(): MatchResponseDto

    @GET("teams")
    suspend fun getTeams(): TeamResponseDto

    @GET("stadiums")
    suspend fun getStadiums(): StadiumResponseDto

    @GET("tournaments")
    suspend fun getTournaments(): TournamentResponseDto

    @POST("payments")
    suspend fun processPayment(
        @Body request: PaymentRequestDto
    ): PaymentResponseDto
}
