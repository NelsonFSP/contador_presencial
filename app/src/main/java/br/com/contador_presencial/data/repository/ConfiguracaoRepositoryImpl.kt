package br.com.contador_presencial.data.repository

import androidx.room.withTransaction
import br.com.contador_presencial.data.local.dao.AutorDao
import br.com.contador_presencial.data.local.dao.EnderecoDao
import br.com.contador_presencial.data.local.database.AppDatabase
import br.com.contador_presencial.data.mapper.toDomain
import br.com.contador_presencial.data.mapper.toEntity
import br.com.contador_presencial.domain.model.Autor
import br.com.contador_presencial.domain.model.ConfiguracaoUsuario
import br.com.contador_presencial.domain.model.Endereco
import br.com.contador_presencial.domain.repository.ConfiguracaoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Implementação de [ConfiguracaoRepository] apoiada no Room.
 */
class ConfiguracaoRepositoryImpl(
    private val autorDao: AutorDao,
    private val enderecoDao: EnderecoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val transactionRunner: suspend (suspend () -> ConfiguracaoUsuario) -> ConfiguracaoUsuario = { block -> block() },
) : ConfiguracaoRepository {
    constructor(
        database: AppDatabase,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) : this(
        autorDao = database.autorDao(),
        enderecoDao = database.enderecoDao(),
        ioDispatcher = ioDispatcher,
        transactionRunner = { block -> database.withTransaction { block() } },
    )

    override fun observeConfiguracao(): Flow<ConfiguracaoUsuario> =
        combine(
            autorDao.observeAll(),
            enderecoDao.observeAll(),
        ) { autores, enderecos ->
            val autor = autores.firstOrNull()?.toDomain()
            val endereco =
                (
                    autor?.let { currentAutor ->
                        enderecos.firstOrNull { it.autorId == currentAutor.id }
                    } ?: enderecos.firstOrNull()
                )?.toDomain()
            ConfiguracaoUsuario(
                autor = autor,
                endereco = endereco,
            )
        }.flowOn(ioDispatcher)

    override suspend fun getConfiguracao(): ConfiguracaoUsuario =
        withContext(ioDispatcher) {
            val autor = autorDao.getAll().firstOrNull()?.toDomain()
            val endereco =
                (
                    autor?.let { currentAutor ->
                        enderecoDao.getByAutorId(currentAutor.id).firstOrNull()
                    } ?: enderecoDao.getAll().firstOrNull()
                )?.toDomain()
            ConfiguracaoUsuario(
                autor = autor,
                endereco = endereco,
            )
        }

    override suspend fun getAutorById(id: UUID): Autor? =
        withContext(ioDispatcher) {
            autorDao.getById(id)?.toDomain()
        }

    override suspend fun getEnderecoById(id: UUID): Endereco? =
        withContext(ioDispatcher) {
            enderecoDao.getById(id)?.toDomain()
        }

    override suspend fun saveConfiguracao(
        autor: Autor,
        endereco: Endereco,
    ): ConfiguracaoUsuario =
        withContext(ioDispatcher) {
            transactionRunner {
                val existingAutor = autorDao.getById(autor.id)
                if (existingAutor == null) {
                    autorDao.insert(autor.toEntity())
                } else {
                    autorDao.update(autor.toEntity())
                }

                val enderecoAssociado = endereco.copy(autorId = endereco.autorId ?: autor.id)
                val existingEndereco = enderecoDao.getById(enderecoAssociado.id)
                if (existingEndereco == null) {
                    enderecoDao.insert(enderecoAssociado.toEntity())
                } else {
                    enderecoDao.update(enderecoAssociado.toEntity())
                }

                ConfiguracaoUsuario(
                    autor = autor,
                    endereco = enderecoAssociado,
                )
            }
        }

    override suspend fun deleteAutor(autor: Autor) {
        withContext(ioDispatcher) {
            autorDao.delete(autor.toEntity())
        }
    }

    override suspend fun deleteEndereco(endereco: Endereco) {
        withContext(ioDispatcher) {
            enderecoDao.delete(endereco.toEntity())
        }
    }
}
