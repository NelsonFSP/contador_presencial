package br.com.contador_presencial.ui.screens.configuration

import br.com.contador_presencial.domain.model.Autor
import br.com.contador_presencial.domain.model.ConfiguracaoUsuario
import br.com.contador_presencial.domain.model.Endereco
import br.com.contador_presencial.domain.model.Periodicidade
import br.com.contador_presencial.domain.repository.ConfiguracaoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ConfigurationViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initLoadsPersistedValuesWithoutOverwritingWithDefaults() =
        runTest(testDispatcher) {
            val autorId = UUID.randomUUID()
            val enderecoId = UUID.randomUUID()
            val repository =
                FakeConfiguracaoRepository(
                    initialConfig =
                        ConfiguracaoUsuario(
                            autor = Autor(id = autorId, periodicidade = Periodicidade.MENSAL),
                            endereco =
                                Endereco(
                                    id = enderecoId,
                                    autorId = autorId,
                                    rua = "Rua Maracá",
                                    numero = 123,
                                    complemento = "Bloco B",
                                    bairro = "Tijuca",
                                    cidade = "Rio de Janeiro",
                                    uf = "RJ",
                                    latitude = -22.9,
                                    longitude = -43.2,
                                ),
                        ),
                )

            val viewModel = ConfigurationViewModel(repository)
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals("Rua Maracá", state.street)
            assertEquals("123", state.number)
            assertEquals("Tijuca", state.neighbor)
            assertEquals("Rio de Janeiro", state.city)
            assertEquals("RJ", state.state)
            assertEquals(Periodicidade.MENSAL, state.periodicidade)
        }

    @Test
    fun saveWithInvalidFieldsReportsValidationErrorsAndDoesNotPersist() =
        runTest(testDispatcher) {
            val repository = FakeConfiguracaoRepository()
            val viewModel = ConfigurationViewModel(repository)
            advanceUntilIdle()

            viewModel.onStreetChange("")
            viewModel.onNumberChange("0")
            viewModel.onStateChange("R")
            viewModel.save()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ConfigurationStatus.ValidationError, state.status)
            assertEquals(ConfigurationValidationError.Required, state.fieldErrors[ConfigurationField.STREET])
            assertEquals(ConfigurationValidationError.InvalidNumber, state.fieldErrors[ConfigurationField.NUMBER])
            assertEquals(ConfigurationValidationError.Required, state.fieldErrors[ConfigurationField.NEIGHBOR])
            assertEquals(ConfigurationValidationError.Required, state.fieldErrors[ConfigurationField.CITY])
            assertEquals(ConfigurationValidationError.InvalidStateCode, state.fieldErrors[ConfigurationField.STATE])
            assertEquals(0, repository.saveCalls)
        }

    @Test
    fun saveInsertsNewConfigurationThenUpdatesExistingPreservingHiddenFields() =
        runTest(testDispatcher) {
            val autorId = UUID.randomUUID()
            val enderecoId = UUID.randomUUID()
            val repository =
                FakeConfiguracaoRepository(
                    initialConfig =
                        ConfiguracaoUsuario(
                            autor = Autor(id = autorId, periodicidade = Periodicidade.MENSAL),
                            endereco =
                                Endereco(
                                    id = enderecoId,
                                    autorId = autorId,
                                    rua = "Rua Antiga",
                                    numero = 10,
                                    complemento = "Sala 302",
                                    bairro = "Centro",
                                    cidade = "Niterói",
                                    uf = "RJ",
                                    latitude = -22.88,
                                    longitude = -43.10,
                                ),
                        ),
                )

            val viewModel = ConfigurationViewModel(repository)
            advanceUntilIdle()

            viewModel.onStreetChange("Rua Nova")
            viewModel.onNumberChange("450")
            viewModel.onStateChange("sp")
            viewModel.save()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertEquals(ConfigurationStatus.Saved, state.status)
            assertTrue(state.fieldErrors.isEmpty())
            assertEquals(1, repository.saveCalls)

            val saved = repository.currentConfig
            assertEquals(autorId, saved.autor?.id)
            assertEquals(enderecoId, saved.endereco?.id)
            assertEquals("Rua Nova", saved.endereco?.rua)
            assertEquals(450, saved.endereco?.numero)
            assertEquals("SP", saved.endereco?.uf)
            assertEquals("Sala 302", saved.endereco?.complemento)
            assertEquals(-22.88, saved.endereco?.latitude)
            assertEquals(-43.10, saved.endereco?.longitude)
        }

    @Test
    fun saveFailureExposesSaveErrorStatus() =
        runTest(testDispatcher) {
            val repository = FakeConfiguracaoRepository(failOnSave = true)
            val viewModel = ConfigurationViewModel(repository)
            advanceUntilIdle()

            viewModel.onStreetChange("Rua Maracá")
            viewModel.onNumberChange("123")
            viewModel.onNeighborChange("Tijuca")
            viewModel.onCityChange("Rio de Janeiro")
            viewModel.onStateChange("RJ")
            viewModel.save()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertFalse(state.isSaving)
            assertEquals(ConfigurationStatus.SaveError, state.status)
        }
}

private class FakeConfiguracaoRepository(
    initialConfig: ConfiguracaoUsuario = ConfiguracaoUsuario(autor = null, endereco = null),
    private val failOnLoad: Boolean = false,
    private val failOnSave: Boolean = false,
) : ConfiguracaoRepository {
    private val stateFlow = MutableStateFlow(initialConfig)
    var currentConfig: ConfiguracaoUsuario = initialConfig
        private set
    var saveCalls: Int = 0
        private set

    override fun observeConfiguracao(): Flow<ConfiguracaoUsuario> = stateFlow

    override suspend fun getConfiguracao(): ConfiguracaoUsuario {
        if (failOnLoad) throw IllegalStateException("Erro ao carregar")
        return currentConfig
    }

    override suspend fun getAutorById(id: UUID): Autor? = currentConfig.autor?.takeIf { it.id == id }

    override suspend fun getEnderecoById(id: UUID): Endereco? = currentConfig.endereco?.takeIf { it.id == id }

    override suspend fun saveConfiguracao(
        autor: Autor,
        endereco: Endereco,
    ): ConfiguracaoUsuario {
        if (failOnSave) throw IllegalStateException("Erro ao salvar")
        saveCalls++
        val saved = ConfiguracaoUsuario(autor = autor, endereco = endereco.copy(autorId = autor.id))
        currentConfig = saved
        stateFlow.value = saved
        return saved
    }

    override suspend fun deleteAutor(autor: Autor) {
        if (currentConfig.autor?.id == autor.id) {
            currentConfig = ConfiguracaoUsuario(autor = null, endereco = null)
            stateFlow.value = currentConfig
        }
    }

    override suspend fun deleteEndereco(endereco: Endereco) {
        if (currentConfig.endereco?.id == endereco.id) {
            currentConfig = currentConfig.copy(endereco = null)
            stateFlow.value = currentConfig
        }
    }
}
