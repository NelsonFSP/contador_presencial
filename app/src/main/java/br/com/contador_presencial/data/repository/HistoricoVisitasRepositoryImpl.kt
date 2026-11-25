package br.com.contador_presencial.data.repository

import br.com.contador_presencial.data.local.dao.HistoricoVisitasDao
import br.com.contador_presencial.data.mapper.toDomain
import br.com.contador_presencial.data.mapper.toEntity
import br.com.contador_presencial.domain.model.HistoricoVisitas
import br.com.contador_presencial.domain.repository.HistoricoVisitasRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Implementação de [HistoricoVisitasRepository] apoiada no Room.
 */
class HistoricoVisitasRepositoryImpl(
    private val historicoVisitasDao: HistoricoVisitasDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : HistoricoVisitasRepository {
    override fun observeAll(): Flow<List<HistoricoVisitas>> =
        historicoVisitasDao
            .observeAll()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(ioDispatcher)

    override suspend fun getAll(): List<HistoricoVisitas> =
        withContext(ioDispatcher) {
            historicoVisitasDao.getAll().map { it.toDomain() }
        }

    override suspend fun getById(id: UUID): HistoricoVisitas? =
        withContext(ioDispatcher) {
            historicoVisitasDao.getById(id)?.toDomain()
        }

    override suspend fun insert(registro: HistoricoVisitas) {
        withContext(ioDispatcher) {
            historicoVisitasDao.insert(registro.toEntity())
        }
    }

    override suspend fun insertAll(registros: List<HistoricoVisitas>) {
        withContext(ioDispatcher) {
            historicoVisitasDao.insertAll(registros.map { it.toEntity() })
        }
    }

    override suspend fun update(registro: HistoricoVisitas) {
        withContext(ioDispatcher) {
            historicoVisitasDao.update(registro.toEntity())
        }
    }

    override suspend fun delete(registro: HistoricoVisitas) {
        withContext(ioDispatcher) {
            historicoVisitasDao.delete(registro.toEntity())
        }
    }

    override suspend fun deleteById(id: UUID) {
        withContext(ioDispatcher) {
            historicoVisitasDao.deleteById(id)
        }
    }
}
