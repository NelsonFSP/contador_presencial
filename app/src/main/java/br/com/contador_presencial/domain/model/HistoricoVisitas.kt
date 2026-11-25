package br.com.contador_presencial.domain.model

import java.util.UUID
import kotlin.time.Duration

/**
 * Representa um registro de permanência no endereço para relatórios futuros.
 */
data class HistoricoVisitas(
    val id: UUID,
    val enderecoId: UUID? = null,
    val horas: Duration,
)
