package br.com.contador_presencial.domain.model

import java.util.UUID

/**
 * Representa o usuário/perfil e sua periodicidade de contagem.
 */
data class Autor(
    val id: UUID,
    val periodicidade: Periodicidade,
)
