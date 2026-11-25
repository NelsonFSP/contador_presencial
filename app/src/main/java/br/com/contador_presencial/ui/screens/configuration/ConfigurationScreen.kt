package br.com.contador_presencial.ui.screens.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.contador_presencial.R
import br.com.contador_presencial.ui.theme.Contador_presencialTheme

/**
 * Stateful composable para a tela de configurações.
 * Gerencia o estado do campo de texto com persistência entre mudanças de configuração.
 */
@Composable
fun ConfigurationScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var targetDays by rememberSaveable { mutableStateOf("") }

    ConfigurationContent(
        targetDays = targetDays,
        onTargetDaysChange = { targetDays = it },
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
    targetDays: String,
    onTargetDaysChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = targetDays,
                onValueChange = onTargetDaysChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.target_days_label)) },
                placeholder = { Text(text = stringResource(R.string.target_days_placeholder)) },
                supportingText = { Text(text = stringResource(R.string.target_days_helper)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                ),
            )

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigurationScreenPreview() {
    Contador_presencialTheme {
        ConfigurationContent(
            targetDays = "10",
            onTargetDaysChange = {},
            onNavigateBack = {},
        )
    }
}
