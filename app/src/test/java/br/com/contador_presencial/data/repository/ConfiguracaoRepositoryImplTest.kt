package br.com.contador_presencial.data.repository

import br.com.contador_presencial.data.local.dao.AutorDao
import br.com.contador_presencial.data.local.dao.EnderecoDao
import br.com.contador_presencial.data.local.entity.AutorEntity
import br.com.contador_presencial.data.local.entity.EnderecoEntity
import br.com.contador_presencial.domain.model.Autor
import br.com.contador_presencial.domain.model.Endereco
import br.com.contador_presencial.domain.model.Periodicidade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.UUID

class ConfiguracaoRepositoryImplTest {
    @Test
    fun saveConfiguracaoInsertsNewRecordsAndLinksAutorId() =
        runTest {
            val autorDao = FakeAutorDao()
            val enderecoDao = FakeEnderecoDao()
            val repository =
                ConfiguracaoRepositoryImpl(
                    autorDao = autorDao,
                    enderecoDao = enderecoDao,
                    ioDispatcher = Dispatchers.Unconfined,
                )

            val autorId = UUID.randomUUID()
            val enderecoId = UUID.randomUUID()
            val autor = Autor(id = autorId, periodicidade = Periodicidade.MENSAL)
            val endereco =
                Endereco(
                    id = enderecoId,
                    autorId = null,
                    rua = "Rua Maracá",
                    numero = 123,
                    complemento = "Sala 4",
                    bairro = "Tijuca",
                    cidade = "Rio de Janeiro",
                    uf = "RJ",
                )

            val saved = repository.saveConfiguracao(autor = autor, endereco = endereco)

            assertEquals(1, autorDao.insertCalls)
            assertEquals(0, autorDao.updateCalls)
            assertEquals(1, enderecoDao.insertCalls)
            assertEquals(0, enderecoDao.updateCalls)
            assertEquals(autorId, saved.endereco?.autorId)

            val loaded = repository.getConfiguracao()
            assertEquals(autor, loaded.autor)
            assertEquals(saved.endereco, loaded.endereco)
        }

    @Test
    fun saveConfiguracaoUpdatesExistingRecordsWithoutDuplicating() =
        runTest {
            val autorDao = FakeAutorDao()
            val enderecoDao = FakeEnderecoDao()
            val repository =
                ConfiguracaoRepositoryImpl(
                    autorDao = autorDao,
                    enderecoDao = enderecoDao,
                    ioDispatcher = Dispatchers.Unconfined,
                )

            val autorId = UUID.randomUUID()
            val enderecoId = UUID.randomUUID()
            val initialAutor = Autor(id = autorId, periodicidade = Periodicidade.SEMANAL)
            val initialEndereco =
                Endereco(
                    id = enderecoId,
                    autorId = autorId,
                    rua = "Av. Paulista",
                    numero = 1000,
                    bairro = "Bela Vista",
                    cidade = "São Paulo",
                    uf = "SP",
                )

            repository.saveConfiguracao(initialAutor, initialEndereco)

            val updatedAutor = initialAutor.copy(periodicidade = Periodicidade.MENSAL)
            val updatedEndereco = initialEndereco.copy(numero = 1500, complemento = "10º andar")
            repository.saveConfiguracao(updatedAutor, updatedEndereco)

            assertEquals(1, autorDao.insertCalls)
            assertEquals(1, autorDao.updateCalls)
            assertEquals(1, enderecoDao.insertCalls)
            assertEquals(1, enderecoDao.updateCalls)
            assertEquals(1, autorDao.getAll().size)
            assertEquals(1, enderecoDao.getAll().size)

            val observed = repository.observeConfiguracao().first()
            assertNotNull(observed.autor)
            assertEquals(Periodicidade.MENSAL, observed.autor?.periodicidade)
            assertEquals(1500, observed.endereco?.numero)
            assertEquals("10º andar", observed.endereco?.complemento)
        }
}

private class FakeAutorDao : AutorDao {
    private val items = linkedMapOf<UUID, AutorEntity>()
    private val flow = MutableStateFlow<List<AutorEntity>>(emptyList())

    var insertCalls = 0
    var updateCalls = 0

    override suspend fun insert(autor: AutorEntity): Long {
        require(!items.containsKey(autor.id)) { "Registro duplicado: ${autor.id}" }
        insertCalls++
        items[autor.id] = autor
        emit()
        return 1L
    }

    override suspend fun insertAll(autores: List<AutorEntity>): List<Long> = autores.map { insert(it) }

    override suspend fun getById(id: UUID): AutorEntity? = items[id]

    override fun observeById(id: UUID): Flow<AutorEntity?> = flow.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun getAll(): List<AutorEntity> = items.values.toList()

    override fun observeAll(): Flow<List<AutorEntity>> = flow

    override suspend fun update(autor: AutorEntity): Int {
        if (!items.containsKey(autor.id)) return 0
        updateCalls++
        items[autor.id] = autor
        emit()
        return 1
    }

    override suspend fun updateAll(autores: List<AutorEntity>): Int = autores.sumOf { update(it) }

    override suspend fun delete(autor: AutorEntity): Int = deleteById(autor.id)

    override suspend fun deleteAll(autores: List<AutorEntity>): Int = autores.sumOf { delete(it) }

    override suspend fun deleteById(id: UUID): Int {
        val removed = items.remove(id) != null
        if (removed) emit()
        return if (removed) 1 else 0
    }

    private fun emit() {
        flow.value = items.values.toList()
    }
}

private class FakeEnderecoDao : EnderecoDao {
    private val items = linkedMapOf<UUID, EnderecoEntity>()
    private val flow = MutableStateFlow<List<EnderecoEntity>>(emptyList())

    var insertCalls = 0
    var updateCalls = 0

    override suspend fun insert(endereco: EnderecoEntity): Long {
        require(!items.containsKey(endereco.id)) { "Registro duplicado: ${endereco.id}" }
        insertCalls++
        items[endereco.id] = endereco
        emit()
        return 1L
    }

    override suspend fun insertAll(enderecos: List<EnderecoEntity>): List<Long> = enderecos.map { insert(it) }

    override suspend fun getById(id: UUID): EnderecoEntity? = items[id]

    override fun observeById(id: UUID): Flow<EnderecoEntity?> = flow.map { list -> list.firstOrNull { it.id == id } }

    override suspend fun getAll(): List<EnderecoEntity> = items.values.toList()

    override fun observeAll(): Flow<List<EnderecoEntity>> = flow

    override suspend fun getByAutorId(autorId: UUID): List<EnderecoEntity> =
        items.values.filter { it.autorId == autorId }

    override suspend fun update(endereco: EnderecoEntity): Int {
        if (!items.containsKey(endereco.id)) return 0
        updateCalls++
        items[endereco.id] = endereco
        emit()
        return 1
    }

    override suspend fun updateAll(enderecos: List<EnderecoEntity>): Int = enderecos.sumOf { update(it) }

    override suspend fun delete(endereco: EnderecoEntity): Int = deleteById(endereco.id)

    override suspend fun deleteAll(enderecos: List<EnderecoEntity>): Int = enderecos.sumOf { delete(it) }

    override suspend fun deleteById(id: UUID): Int {
        val removed = items.remove(id) != null
        if (removed) emit()
        return if (removed) 1 else 0
    }

    private fun emit() {
        flow.value = items.values.toList()
    }
}
