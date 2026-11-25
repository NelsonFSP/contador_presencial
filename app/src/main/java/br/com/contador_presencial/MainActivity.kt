package br.com.contador_presencial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.contador_presencial.navigation.AppNavHost
import br.com.contador_presencial.ui.theme.Contador_presencialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Contador_presencialTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun ContadorScreen(
    onNavigateToConfiguration: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var count by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.presencial_realizado),
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.displayLarge,
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = onNavigateToConfiguration,
                ) {
                    Text(text = stringResource(R.string.configurations))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContadorScreenPreview() {
    Contador_presencialTheme {
        ContadorScreen(
            onNavigateToConfiguration = {},
        )
    }
}
