package com.app.partidos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.partidos.data.local.entity.CompraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompra(compra: CompraEntity)

    @Query("SELECT * FROM compras WHERE usuarioId = :usuarioId ORDER BY fechaCompra DESC")
    fun getComprasByUsuarioFlow(usuarioId: String): Flow<List<CompraEntity>>

    @Query("SELECT * FROM compras WHERE id = :compraId LIMIT 1")
    suspend fun getCompraById(compraId: String): CompraEntity?
}
