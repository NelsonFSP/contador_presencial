package br.com.contador_presencial.domain.model

import java.util.UUID

/**
 * Representa o local cujo registro de presença será associado ao usuário.
 */
data class Endereco(
    val id: UUID,
    val autorId: UUID? = null,
    val rua: String,
    val numero: Int,
    val complemento: String? = null,
    val bairro: String,
    val cidade: String,
    val uf: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
)
