package br.com.contador_presencial.data.local

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.contador_presencial.data.local.dao.AutorDao
import br.com.contador_presencial.data.local.dao.EnderecoDao
import br.com.contador_presencial.data.local.dao.HistoricoVisitasDao
import br.com.contador_presencial.data.local.database.AppDatabase
import br.com.contador_presencial.data.local.entity.AutorEntity
import br.com.contador_presencial.data.local.entity.EnderecoEntity
import br.com.contador_presencial.data.local.entity.HistoricoVisitasEntity
import br.com.contador_presencial.domain.model.Periodicidade
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@RunWith(AndroidJUnit4::class)
class AppDatabaseDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var autorDao: AutorDao
    private lateinit var enderecoDao: EnderecoDao
    private lateinit var historicoDao: HistoricoVisitasDao

    @Before
    fun createDb() {
        database =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java,
            ).build()
        autorDao = database.autorDao()
        enderecoDao = database.enderecoDao()
        historicoDao = database.historicoVisitasDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun crudOperationsWorkAcrossAllEntities() =
        runTest {
            val autor1 = AutorEntity(id = UUID.randomUUID(), periodicidade = Periodicidade.SEMANAL)
            val autor2 = AutorEntity(id = UUID.randomUUID(), periodicidade = Periodicidade.MENSAL)
            autorDao.insert(autor1)
            autorDao.insertAll(listOf(autor2))
            assertEquals(2, autorDao.getAll().size)
            assertEquals(autor1, autorDao.getById(autor1.id))

            val updatedAutor1 = autor1.copy(periodicidade = Periodicidade.MENSAL)
            autorDao.update(updatedAutor1)
            assertEquals(Periodicidade.MENSAL, autorDao.getById(autor1.id)?.periodicidade)

            val endereco1 =
                EnderecoEntity(
                    id = UUID.randomUUID(),
                    autorId = autor1.id,
                    rua = "Rua Maracá",
                    numero = 123,
                    complemento = "Sala 2",
                    bairro = "Tijuca",
                    cidade = "Rio de Janeiro",
                    uf = "RJ",
                    latitude = -22.9,
                    longitude = -43.2,
                )
            enderecoDao.insert(endereco1)
            assertEquals(endereco1, enderecoDao.getById(endereco1.id))

            val visita1 =
                HistoricoVisitasEntity(
                    id = UUID.randomUUID(),
                    enderecoId = endereco1.id,
                    horas = 8.hours + 15.minutes,
                )
            historicoDao.insert(visita1)
            assertEquals(visita1, historicoDao.getById(visita1.id))

            val updatedVisita = visita1.copy(horas = 9.hours)
            historicoDao.update(updatedVisita)
            assertEquals(9.hours.inWholeMilliseconds, historicoDao.getById(visita1.id)?.horas?.inWholeMilliseconds)

            historicoDao.delete(updatedVisita)
            assertNull(historicoDao.getById(visita1.id))
        }

    @Test(expected = SQLiteConstraintException::class)
    fun insertingDuplicatePrimaryKeyAbortsInsteadOfReplacing() =
        runTest {
            val id = UUID.randomUUID()
            autorDao.insert(AutorEntity(id = id, periodicidade = Periodicidade.SEMANAL))
            autorDao.insert(AutorEntity(id = id, periodicidade = Periodicidade.MENSAL))
        }

    @Test
    fun deletingAutorCascadesToEnderecoAndHistoricoWithoutOrphans() =
        runTest {
            val autor = AutorEntity(id = UUID.randomUUID(), periodicidade = Periodicidade.SEMANAL)
            val endereco =
                EnderecoEntity(
                    id = UUID.randomUUID(),
                    autorId = autor.id,
                    rua = "Av. Brasil",
                    numero = 500,
                    bairro = "Centro",
                    cidade = "Rio de Janeiro",
                    uf = "RJ",
                )
            val visita =
                HistoricoVisitasEntity(
                    id = UUID.randomUUID(),
                    enderecoId = endereco.id,
                    horas = 6.hours,
                )

            autorDao.insert(autor)
            enderecoDao.insert(endereco)
            historicoDao.insert(visita)

            autorDao.deleteById(autor.id)

            assertTrue(autorDao.getAll().isEmpty())
            assertTrue(enderecoDao.getAll().isEmpty())
            assertTrue(historicoDao.getAll().isEmpty())
        }
}
