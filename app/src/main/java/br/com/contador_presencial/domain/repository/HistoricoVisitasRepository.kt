package br.com.contador_presencial.domain.repository

import br.com.contador_presencial.domain.model.HistoricoVisitas
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Contrato de persistência para o histórico de visitas.
 */
interface HistoricoVisitasRepository {
    fun observeAll(): Flow<List<HistoricoVisitas>>

    suspend fun getAll(): List<HistoricoVisitas>

    suspend fun getById(id: UUID): HistoricoVisitas?

    suspend fun insert(registro: HistoricoVisitas)

    suspend fun insertAll(registros: List<HistoricoVisitas>)

    suspend fun update(registro: HistoricoVisitas)

    suspend fun delete(registro: HistoricoVisitas)

    suspend fun deleteById(id: UUID)
}
