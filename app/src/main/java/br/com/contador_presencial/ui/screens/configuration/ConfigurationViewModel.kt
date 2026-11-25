package br.com.contador_presencial.ui.screens.configuration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.contador_presencial.domain.model.Autor
import br.com.contador_presencial.domain.model.Endereco
import br.com.contador_presencial.domain.model.Periodicidade
import br.com.contador_presencial.domain.repository.ConfiguracaoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel responsável por carregar, validar e persistir as configurações do usuário.
 */
class ConfigurationViewModel(
    private val configuracaoRepository: ConfiguracaoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConfigurationUiState())
    val uiState: StateFlow<ConfigurationUiState> = _uiState.asStateFlow()

    private var persistedAutorId: UUID? = null
    private var persistedEnderecoId: UUID? = null
    private var persistedComplemento: String? = null
    private var persistedLatitude: Double? = null
    private var persistedLongitude: Double? = null
    private var hasUserEdited: Boolean = false

    init {
        loadConfiguration()
    }

    private fun loadConfiguration() {
        viewModelScope.launch {
            try {
                val (autor, endereco) = configuracaoRepository.getConfiguracao()
                persistedAutorId = autor?.id
                persistedEnderecoId = endereco?.id
                persistedComplemento = endereco?.complemento
                persistedLatitude = endereco?.latitude
                persistedLongitude = endereco?.longitude

                _uiState.update { current ->
                    if (hasUserEdited) {
                        current.copy(isLoading = false)
                    } else {
                        current.copy(
                            street = endereco?.rua ?: current.street,
                            number = endereco?.numero?.toString() ?: current.number,
                            neighbor = endereco?.bairro ?: current.neighbor,
                            city = endereco?.cidade ?: current.city,
                            state = endereco?.uf ?: current.state,
                            periodicidade = autor?.periodicidade ?: current.periodicidade,
                            isLoading = false,
                        )
                    }
                }
            } catch (ce: CancellationException) {
                throw ce
            } catch (_: Exception) {
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        status = ConfigurationStatus.LoadError,
                    )
                }
            }
        }
    }

    fun onStreetChange(value: String) {
        hasUserEdited = true
        _uiState.update { current ->
            current.copy(
                street = value,
                fieldErrors = current.fieldErrors - ConfigurationField.STREET,
                status = ConfigurationStatus.Idle,
            )
        }
    }

    fun onNumberChange(value: String) {
        if (!value.all { it.isDigit() }) return
        hasUserEdited = true
        _uiState.update { current ->
            current.copy(
                number = value,
                fieldErrors = current.fieldErrors - ConfigurationField.NUMBER,
                status = ConfigurationStatus.Idle,
            )
        }
    }

    fun onNeighborChange(value: String) {
        hasUserEdited = true
        _uiState.update { current ->
            current.copy(
                neighbor = value,
                fieldErrors = current.fieldErrors - ConfigurationField.NEIGHBOR,
                status = ConfigurationStatus.Idle,
            )
        }
    }

    fun onCityChange(value: String) {
        hasUserEdited = true
        _uiState.update { current ->
            current.copy(
                city = value,
                fieldErrors = current.fieldErrors - ConfigurationField.CITY,
                status = ConfigurationStatus.Idle,
            )
        }
    }

    fun onStateChange(value: String) {
        hasUserEdited = true
        val normalized = value.take(2).uppercase()
        _uiState.update { current ->
            current.copy(
                state = normalized,
                fieldErrors = current.fieldErrors - ConfigurationField.STATE,
                status = ConfigurationStatus.Idle,
            )
        }
    }

    fun onPeriodicidadeChange(periodicidade: Periodicidade) {
        hasUserEdited = true
        _uiState.update { current ->
            current.copy(
                periodicidade = periodicidade,
                status = ConfigurationStatus.Idle,
            )
        }
    }

    fun consumeStatus() {
        _uiState.update { current ->
            if (current.status == ConfigurationStatus.Idle) {
                current
            } else {
                current.copy(status = ConfigurationStatus.Idle)
            }
        }
    }

    fun save() {
        val current = _uiState.value
        if (current.isSaving || current.isLoading) return

        val validationErrors = validate(current)
        if (validationErrors.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    fieldErrors = validationErrors,
                    status = ConfigurationStatus.ValidationError,
                )
            }
            return
        }

        val parsedNumber = current.number.trim().toInt()
        val normalizedStreet = current.street.trim()
        val normalizedNeighbor = current.neighbor.trim()
        val normalizedCity = current.city.trim()
        val normalizedState = current.state.trim().uppercase()

        val autorId = persistedAutorId ?: UUID.randomUUID()
        val enderecoId = persistedEnderecoId ?: UUID.randomUUID()

        val autor =
            Autor(
                id = autorId,
                periodicidade = current.periodicidade,
            )
        val endereco =
            Endereco(
                id = enderecoId,
                autorId = autorId,
                rua = normalizedStreet,
                numero = parsedNumber,
                complemento = persistedComplemento,
                bairro = normalizedNeighbor,
                cidade = normalizedCity,
                uf = normalizedState,
                latitude = persistedLatitude,
                longitude = persistedLongitude,
            )

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSaving = true,
                    fieldErrors = emptyMap(),
                    status = ConfigurationStatus.Idle,
                )
            }
            try {
                val saved = configuracaoRepository.saveConfiguracao(autor = autor, endereco = endereco)
                persistedAutorId = saved.autor?.id ?: autorId
                persistedEnderecoId = saved.endereco?.id ?: enderecoId
                persistedComplemento = saved.endereco?.complemento
                persistedLatitude = saved.endereco?.latitude
                persistedLongitude = saved.endereco?.longitude

                _uiState.update { state ->
                    state.copy(
                        street = normalizedStreet,
                        number = parsedNumber.toString(),
                        neighbor = normalizedNeighbor,
                        city = normalizedCity,
                        state = normalizedState,
                        isSaving = false,
                        status = ConfigurationStatus.Saved,
                    )
                }
            } catch (ce: CancellationException) {
                throw ce
            } catch (_: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isSaving = false,
                        status = ConfigurationStatus.SaveError,
                    )
                }
            }
        }
    }

    private fun validate(state: ConfigurationUiState): Map<ConfigurationField, ConfigurationValidationError> {
        val errors = mutableMapOf<ConfigurationField, ConfigurationValidationError>()

        if (state.street.isBlank()) {
            errors[ConfigurationField.STREET] = ConfigurationValidationError.Required
        }

        val trimmedNumber = state.number.trim()
        if (trimmedNumber.isEmpty()) {
            errors[ConfigurationField.NUMBER] = ConfigurationValidationError.Required
        } else {
            val numericValue = trimmedNumber.toIntOrNull()
            if (numericValue == null || numericValue <= 0) {
                errors[ConfigurationField.NUMBER] = ConfigurationValidationError.InvalidNumber
            }
        }

        if (state.neighbor.isBlank()) {
            errors[ConfigurationField.NEIGHBOR] = ConfigurationValidationError.Required
        }

        if (state.city.isBlank()) {
            errors[ConfigurationField.CITY] = ConfigurationValidationError.Required
        }

        val trimmedState = state.state.trim()
        if (trimmedState.isEmpty()) {
            errors[ConfigurationField.STATE] = ConfigurationValidationError.Required
        } else if (trimmedState.length != 2 || !trimmedState.all(Char::isLetter)) {
            errors[ConfigurationField.STATE] = ConfigurationValidationError.InvalidStateCode
        }

        return errors
    }

    companion object {
        fun provideFactory(configuracaoRepository: ConfiguracaoRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ConfigurationViewModel(configuracaoRepository = configuracaoRepository)
                }
            }
    }
}
