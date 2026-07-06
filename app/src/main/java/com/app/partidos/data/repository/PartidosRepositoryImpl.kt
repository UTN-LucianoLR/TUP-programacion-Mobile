package com.app.partidos.data.repository

import androidx.room.withTransaction


import com.app.partidos.data.local.AppDatabase
import com.app.partidos.data.local.entity.CompraEntity
import com.app.partidos.data.local.entity.PartidoEntity
import com.app.partidos.data.mapper.toDomain
import com.app.partidos.data.remote.PartidosApi
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
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PartidosRepositoryImpl(
    private val api: PartidosApi,
    private val db: AppDatabase
) : PartidosRepository {

    private val partidoDao = db.partidoDao()
    private val compraDao = db.compraDao()

    override suspend fun refreshPartidos(): Result<Unit> {
        return try {
            val partidosDto = api.getPartidos()
            val entities = partidosDto.map {
                PartidoEntity(
                    id = it.id.toString(),
                    equipoLocal = it.equipoLocal ?: "Desconocido",
                    codigoLocal = it.equipoLocal?.take(3)?.uppercase() ?: "XXX",
                    equipoVisitante = it.equipoVisitante ?: "Desconocido",
                    codigoVisitante = it.equipoVisitante?.take(3)?.uppercase() ?: "XXX",
                    estadio = it.estadioNombre ?: "Desconocido",
                    fecha = it.fecha ?: "",
                    hora = it.hora ?: "",
                    precio = 50.0, // Valor por defecto
                    entradasDisponibles = 100,
                    dateIso = "${it.fecha ?: ""}T${it.hora ?: ""}"
                )
            }
            db.withTransaction {
                partidoDao.deleteAllPartidos()
                partidoDao.insertPartidos(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPastPartidosFlow(query: String): Flow<List<Partido>> {
        val nowIso = Instant.now().atZone(ZoneId.of("America/Argentina/Buenos_Aires"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        
        return partidoDao.getPastPartidosFlow(query, nowIso).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getUpcomingPartidosFlow(query: String): Flow<List<Partido>> {
        val nowIso = Instant.now().atZone(ZoneId.of("America/Argentina/Buenos_Aires"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        
        return partidoDao.getUpcomingPartidosFlow(query, nowIso).map { list ->
            list.map { it.toDomain() }
        }
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
                    asiento = java.util.UUID.randomUUID().toString().take(6),
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
            
            if (response.id >= 0) {
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
