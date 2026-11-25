package br.com.contador_presencial.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.contador_presencial.data.local.entity.HistoricoVisitasEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * DAO completo para operações CRUD da tabela `historico_visitas`.
 */
@Dao
interface HistoricoVisitasDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(registro: HistoricoVisitasEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(registros: List<HistoricoVisitasEntity>): List<Long>

    @Query("SELECT * FROM historico_visitas WHERE id = :id LIMIT 1")
    suspend fun getById(id: UUID): HistoricoVisitasEntity?

    @Query("SELECT * FROM historico_visitas WHERE id = :id LIMIT 1")
    fun observeById(id: UUID): Flow<HistoricoVisitasEntity?>

    @Query("SELECT * FROM historico_visitas")
    suspend fun getAll(): List<HistoricoVisitasEntity>

    @Query("SELECT * FROM historico_visitas")
    fun observeAll(): Flow<List<HistoricoVisitasEntity>>

    @Query("SELECT * FROM historico_visitas WHERE endereco_id = :enderecoId")
    suspend fun getByEnderecoId(enderecoId: UUID): List<HistoricoVisitasEntity>

    @Update
    suspend fun update(registro: HistoricoVisitasEntity): Int

    @Update
    suspend fun updateAll(registros: List<HistoricoVisitasEntity>): Int

    @Delete
    suspend fun delete(registro: HistoricoVisitasEntity): Int

    @Delete
    suspend fun deleteAll(registros: List<HistoricoVisitasEntity>): Int

    @Query("DELETE FROM historico_visitas WHERE id = :id")
    suspend fun deleteById(id: UUID): Int
}
