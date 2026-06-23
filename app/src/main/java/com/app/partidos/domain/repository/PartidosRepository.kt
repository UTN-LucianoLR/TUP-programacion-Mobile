package com.app.partidos.domain.repository

import com.app.partidos.domain.model.Compra
import com.app.partidos.domain.model.Pago
import com.app.partidos.domain.model.Partido
import kotlinx.coroutines.flow.Flow

interface PartidosRepository {
    suspend fun getPartidos(forceRefresh: Boolean = false): Result<Unit>
    
    fun getPartidosFlow(): Flow<List<Partido>>
    
    suspend fun getPartidoById(partidoId: String): Partido?
    
    
    suspend fun procesarCompra(pago: Pago, usuarioId: String, partidoId: String, cantidad: Int): Result<Compra>
    
    fun getHistorialComprasFlow(usuarioId: String): Flow<List<Compra>>
}
