package com.app.partidos.data.repository

import com.app.partidos.data.local.dao.CompraDao
import com.app.partidos.data.local.dao.PartidoDao
import com.app.partidos.data.local.entity.CompraEntity
import com.app.partidos.data.remote.PartidosApi
import com.app.partidos.data.remote.dto.PaymentRequestDto
import com.app.partidos.domain.model.Compra
import com.app.partidos.domain.model.Pago
import com.app.partidos.domain.model.Partido
import com.app.partidos.domain.repository.PartidosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class PartidosRepositoryImpl(
    private val api: PartidosApi,
    private val partidoDao: PartidoDao,
    private val compraDao: CompraDao
) : PartidosRepository {

    override suspend fun getPartidos(forceRefresh: Boolean): Result<Unit> {
        return try {
            val count = partidoDao.countPartidos()
            if (count == 0 || forceRefresh) {
                val matchesResponse = api.getMatches()
                val teamsResponse = api.getTeams()
                val stadiumsResponse = api.getStadiums()
                // api.getTournaments() is available but not used for entities yet
                
                val teamsMap = teamsResponse.teams.associateBy { it.idTeam }
                val stadiumsMap = stadiumsResponse.stadiums.associateBy { it.idStadium }

                val entities = matchesResponse.matches.map { matchDto ->
                    val homeTeam = teamsMap[matchDto.homeTeamId]
                    val awayTeam = teamsMap[matchDto.awayTeamId]
                    val stadium = stadiumsMap[matchDto.stadiumId]
                    
                    matchDto.toEntity(homeTeam, awayTeam, stadium)
                }
                partidoDao.deleteAllPartidos()
                partidoDao.insertPartidos(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPartidosFlow(): Flow<List<Partido>> {
        return partidoDao.getPartidosFlow().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getPartidoById(partidoId: String): Partido? {
        return partidoDao.getPartidoById(partidoId)?.toDomain()
    }

    override suspend fun procesarCompra(pago: Pago, usuarioId: String, partidoId: String, cantidad: Int): Result<Compra> {
        return try {
            val request = PaymentRequestDto(
                matchId = partidoId,
                userId = usuarioId,
                quantity = cantidad,
                cardNumber = pago.numeroTarjeta,
                cardHolder = pago.nombreTitular,
                expiryDate = pago.vencimiento,
                cvv = pago.cvv
            )

            val response = api.processPayment(request)
            
            if (response.success && response.transactionId != null) {
                val compraEntity = CompraEntity(
                    usuarioId = usuarioId,
                    partidoId = partidoId,
                    cantidadEntradas = cantidad,
                    total = response.totalAmount,
                    fechaCompra = response.purchaseDate ?: Instant.now().toString()
                )
                compraDao.insertCompra(compraEntity)
                partidoDao.restarEntradas(partidoId, cantidad) // Descontar entradas
                Result.success(compraEntity.toDomain())
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error procesando pago: ${e.message}"))
        }
    }

    override fun getHistorialComprasFlow(usuarioId: String): Flow<List<Compra>> {
        return compraDao.getComprasByUsuarioFlow(usuarioId).map { list -> list.map { it.toDomain() } }
    }
}
