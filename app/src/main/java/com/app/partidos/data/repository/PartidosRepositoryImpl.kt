package com.app.partidos.data.repository

import com.app.partidos.data.local.dao.CompraDao
import com.app.partidos.data.local.dao.PartidoDao
import com.app.partidos.data.local.entity.CompraEntity
import com.app.partidos.data.mapper.toDomain
import com.app.partidos.data.mapper.toEntity
import com.app.partidos.data.remote.PartidosApi
import com.app.partidos.data.remote.dto.CompraResponseDto
import com.app.partidos.data.remote.dto.CrearCompraDto
import com.app.partidos.data.remote.dto.CrearPagoDto
import com.app.partidos.data.remote.dto.CrearTicketDto
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
                val matchesResponse = api.getPartidos()
                val entities = matchesResponse.map { it.toEntity() }
                
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
            val tickets = (1..cantidad).map {
                CrearTicketDto(
                    partidoId = partidoId.toInt(),
                    sector = "General",
                    fila = "A",
                    asiento = it.toString(),
                    precio = pago.monto / cantidad
                )
            }

            val request = CrearCompraDto(
                usuarioId = usuarioId.toInt(),
                pago = CrearPagoDto(
                    metodoPago    = pago.metodoPago,
                    monto         = pago.monto,
                    numeroTarjeta = pago.numeroTarjeta,
                    titular       = pago.nombreTitular,
                    vencimiento   = pago.vencimiento,
                    cvv           = pago.cvv
                ),
                tickets = tickets
            )

            val response = api.processPayment(request)
            
            if (response.id > 0) {
                val compraEntity = CompraEntity(
                    usuarioId = usuarioId,
                    partidoId = partidoId,
                    cantidadEntradas = cantidad,
                    total = response.total,
                    fechaCompra = response.fecha
                )
                compraDao.insertCompra(compraEntity)
                partidoDao.restarEntradas(partidoId, cantidad)
                Result.success(compraEntity.toDomain())
            } else {
                Result.failure(Exception("La respuesta de la API no contiene un ID válido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getHistorialComprasFlow(usuarioId: String): Flow<List<Compra>> {
        return compraDao.getComprasByUsuarioFlow(usuarioId).map { list -> list.map { it.toDomain() } }
    }
}
