package br.com.contador_presencial.ui.screens.configuration

import br.com.contador_presencial.domain.model.Periodicidade

enum class ConfigurationField {
    STREET,
    NUMBER,
    NEIGHBOR,
    CITY,
    STATE,
}

sealed interface ConfigurationValidationError {
    data object Required : ConfigurationValidationError

    data object InvalidNumber : ConfigurationValidationError

    data object InvalidStateCode : ConfigurationValidationError
}

sealed interface ConfigurationStatus {
    data object Idle : ConfigurationStatus

    data object Saved : ConfigurationStatus

    data object LoadError : ConfigurationStatus

    data object SaveError : ConfigurationStatus

    data object ValidationError : ConfigurationStatus
}

data class ConfigurationUiState(
    val street: String = "",
    val number: String = "",
    val neighbor: String = "",
    val city: String = "",
    val state: String = "",
    val periodicidade: Periodicidade = Periodicidade.SEMANAL,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val fieldErrors: Map<ConfigurationField, ConfigurationValidationError> = emptyMap(),
    val status: ConfigurationStatus = ConfigurationStatus.Idle,
)
