package br.com.contador_presencial.ui.screens.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.contador_presencial.ContadorPresencialApplication
import br.com.contador_presencial.R
import br.com.contador_presencial.domain.model.Periodicidade
import br.com.contador_presencial.ui.theme.Contador_presencialTheme

/**
 * Stateful composable para a tela de configurações.
 * Conecta o [ConfigurationViewModel] ao conteúdo visual desacoplado.
 */
@Composable
fun ConfigurationScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val application = LocalContext.current.applicationContext as ContadorPresencialApplication
    val viewModel: ConfigurationViewModel =
        viewModel(
            factory =
                ConfigurationViewModel.provideFactory(
                    configuracaoRepository = application.appContainer.configuracaoRepository,
                ),
        )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ConfigurationContent(
        uiState = uiState,
        onStreetChange = viewModel::onStreetChange,
        onNumberChange = viewModel::onNumberChange,
        onNeighborChange = viewModel::onNeighborChange,
        onCityChange = viewModel::onCityChange,
        onStateChange = viewModel::onStateChange,
        onPeriodicidadeChange = viewModel::onPeriodicidadeChange,
        onSave = viewModel::save,
        onStatusConsumed = viewModel::consumeStatus,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

/**
 * Stateless composable para a interface da tela de configurações.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationContent(
    uiState: ConfigurationUiState,
    onStreetChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onNeighborChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onStateChange: (String) -> Unit,
    onPeriodicidadeChange: (Periodicidade) -> Unit,
    onSave: () -> Unit,
    onStatusConsumed: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    val statusMessage =
        when (uiState.status) {
            ConfigurationStatus.Idle -> null
            ConfigurationStatus.Saved -> stringResource(R.string.configuration_saved_success)
            ConfigurationStatus.LoadError -> stringResource(R.string.configuration_load_error)
            ConfigurationStatus.SaveError -> stringResource(R.string.configuration_save_error)
            ConfigurationStatus.ValidationError -> stringResource(R.string.configuration_validation_error)
        }

    LaunchedEffect(uiState.status, statusMessage) {
        if (statusMessage != null) {
            snackbarHostState.showSnackbar(statusMessage)
            onStatusConsumed()
        }
    }

    val periodicidadeOptions =
        listOf(
            Periodicidade.SEMANAL to stringResource(R.string.period_weekly),
            Periodicidade.MENSAL to stringResource(R.string.period_monthly),
        )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.configuration_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val streetError = uiState.fieldErrors[ConfigurationField.STREET]
            OutlinedTextField(
                value = uiState.street,
                onValueChange = onStreetChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.street_office_address_label)) },
                placeholder = { Text(text = stringResource(R.string.street_office_placeholder)) },
                isError = streetError != null,
                supportingText = validationSupportingText(streetError),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
            )

            val numberError = uiState.fieldErrors[ConfigurationField.NUMBER]
            OutlinedTextField(
                value = uiState.number,
                onValueChange = onNumberChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.number_office_address_label)) },
                placeholder = { Text(text = stringResource(R.string.number_office_placeholder)) },
                isError = numberError != null,
                supportingText = validationSupportingText(numberError),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
            )

            val neighborError = uiState.fieldErrors[ConfigurationField.NEIGHBOR]
            OutlinedTextField(
                value = uiState.neighbor,
                onValueChange = onNeighborChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.neighbor_office_address_label)) },
                placeholder = { Text(text = stringResource(R.string.neighbor_office_placeholder)) },
                isError = neighborError != null,
                supportingText = validationSupportingText(neighborError),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
            )

            val cityError = uiState.fieldErrors[ConfigurationField.CITY]
            OutlinedTextField(
                value = uiState.city,
                onValueChange = onCityChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.city_office_address_label)) },
                placeholder = { Text(text = stringResource(R.string.city_office_placeholder)) },
                isError = cityError != null,
                supportingText = validationSupportingText(cityError),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
            )

            val stateError = uiState.fieldErrors[ConfigurationField.STATE]
            OutlinedTextField(
                value = uiState.state,
                onValueChange = onStateChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.state_office_address_label)) },
                placeholder = { Text(text = stringResource(R.string.state_office_placeholder)) },
                isError = stateError != null,
                supportingText = validationSupportingText(stateError),
                singleLine = true,
                keyboardOptions =
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.target_períodos_label))
            }

            Column(modifier = Modifier.selectableGroup()) {
                periodicidadeOptions.forEach { (option, label) ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (option == uiState.periodicidade),
                                    onClick = { onPeriodicidadeChange(option) },
                                    role = Role.RadioButton,
                                )
                                .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (option == uiState.periodicidade),
                            onClick = null,
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onSave()
                },
                enabled = !uiState.isLoading && !uiState.isSaving,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text =
                        if (uiState.isSaving) {
                            stringResource(R.string.saving)
                        } else {
                            stringResource(R.string.save)
                        },
                )
            }
        }
    }
}

@Composable
private fun validationSupportingText(error: ConfigurationValidationError?): (@Composable () -> Unit)? =
    when (error) {
        null -> null
        ConfigurationValidationError.Required -> {
            { Text(text = stringResource(R.string.validation_required_field)) }
        }
        ConfigurationValidationError.InvalidNumber -> {
            { Text(text = stringResource(R.string.validation_invalid_number)) }
        }
        ConfigurationValidationError.InvalidStateCode -> {
            { Text(text = stringResource(R.string.validation_invalid_uf)) }
        }
    }

@Preview(showBackground = true)
@Composable
fun ConfigurationScreenPreview() {
    Contador_presencialTheme {
        ConfigurationContent(
            uiState = ConfigurationUiState(isLoading = false),
            onStreetChange = {},
            onNumberChange = {},
            onNeighborChange = {},
            onCityChange = {},
            onStateChange = {},
            onPeriodicidadeChange = {},
            onSave = {},
            onStatusConsumed = {},
            onNavigateBack = {},
        )
    }
}
