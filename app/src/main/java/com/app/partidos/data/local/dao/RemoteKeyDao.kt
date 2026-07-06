package com.app.partidos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.partidos.data.local.entity.PartidoRemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PartidoRemoteKeyEntity>)

    @Query("SELECT * FROM partidos_remote_keys WHERE id = :id")
    suspend fun remoteKeysPartidoId(id: String): PartidoRemoteKeyEntity?

    @Query("DELETE FROM partidos_remote_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT MAX(nextPage) FROM partidos_remote_keys")
    suspend fun getHighestNextPage(): Int?
}
