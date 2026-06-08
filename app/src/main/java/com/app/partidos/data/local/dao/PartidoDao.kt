package com.app.partidos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.partidos.data.local.entity.PartidoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartidos(partidos: List<PartidoEntity>)

    @Query("SELECT * FROM partidos ORDER BY dateIso ASC")
    fun getPartidosFlow(): Flow<List<PartidoEntity>>

    @Query("SELECT * FROM partidos WHERE id = :partidoId LIMIT 1")
    suspend fun getPartidoById(partidoId: String): PartidoEntity? // partidoId ahora es String

    @Query("SELECT COUNT(*) FROM partidos")
    suspend fun countPartidos(): Int

    @Query("DELETE FROM partidos")
    suspend fun deleteAllPartidos()

    @Query("UPDATE partidos SET entradasDisponibles = entradasDisponibles - :cantidad WHERE id = :partidoId")
    suspend fun restarEntradas(partidoId: String, cantidad: Int)
}
