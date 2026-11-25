package br.com.contador_presencial.di

import android.content.Context
import br.com.contador_presencial.data.local.database.AppDatabase
import br.com.contador_presencial.data.repository.ConfiguracaoRepositoryImpl
import br.com.contador_presencial.data.repository.HistoricoVisitasRepositoryImpl
import br.com.contador_presencial.domain.repository.ConfiguracaoRepository
import br.com.contador_presencial.domain.repository.HistoricoVisitasRepository

/**
 * Contêiner manual de dependências compartilhadas pela aplicação.
 */
interface AppContainer {
    val configuracaoRepository: ConfiguracaoRepository
    val historicoVisitasRepository: HistoricoVisitasRepository
}

class DefaultAppContainer(
    private val context: Context,
) : AppContainer {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    override val configuracaoRepository: ConfiguracaoRepository by lazy {
        ConfiguracaoRepositoryImpl(database = database)
    }

    override val historicoVisitasRepository: HistoricoVisitasRepository by lazy {
        HistoricoVisitasRepositoryImpl(historicoVisitasDao = database.historicoVisitasDao())
    }
}
