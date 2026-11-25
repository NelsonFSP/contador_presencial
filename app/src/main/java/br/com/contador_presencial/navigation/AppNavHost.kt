package br.com.contador_presencial.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.contador_presencial.ContadorScreen
import br.com.contador_presencial.ui.screens.configuration.ConfigurationScreen

/**
 * Host de navegação da aplicação utilizando Navigation Compose tipado.
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Route = Route.Home,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Route.Home> {
            ContadorScreen(
                onNavigateToConfiguration = {
                    navController.navigate(Route.Configuration)
                },
            )
        }
        composable<Route.Configuration> {
            ConfigurationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
