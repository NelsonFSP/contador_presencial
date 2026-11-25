package br.com.contador_presencial.navigation

import kotlinx.serialization.Serializable

/**
 * Definições tipadas das rotas de navegação do aplicativo.
 */
sealed interface Route {
    @Serializable
    data object Home : Route

    @Serializable
    data object Configuration : Route
}
