package br.com.contador_presencial.domain.model

/**
 * Agrupamento das configurações persistidas do usuário (autor e endereço).
 */
data class ConfiguracaoUsuario(
    val autor: Autor?,
    val endereco: Endereco?,
)
