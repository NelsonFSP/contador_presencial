package br.com.contador_presencial.domain.repository

import br.com.contador_presencial.domain.model.Autor
import br.com.contador_presencial.domain.model.ConfiguracaoUsuario
import br.com.contador_presencial.domain.model.Endereco
import kotlinx.coroutines.flow.Flow
import java.util.UUID

/**
 * Contrato de persistência para configurações de autor e endereço.
 */
interface ConfiguracaoRepository {
    fun observeConfiguracao(): Flow<ConfiguracaoUsuario>

    suspend fun getConfiguracao(): ConfiguracaoUsuario

    suspend fun getAutorById(id: UUID): Autor?

    suspend fun getEnderecoById(id: UUID): Endereco?

    suspend fun saveConfiguracao(autor: Autor, endereco: Endereco): ConfiguracaoUsuario

    suspend fun deleteAutor(autor: Autor)

    suspend fun deleteEndereco(endereco: Endereco)
}
