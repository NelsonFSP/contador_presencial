package br.com.contador_presencial

import android.app.Application
import br.com.contador_presencial.di.AppContainer
import br.com.contador_presencial.di.DefaultAppContainer

/**
 * Classe [Application] responsável por fornecer o contêiner de dependências do app.
 */
class ContadorPresencialApplication : Application() {
    val appContainer: AppContainer by lazy {
        DefaultAppContainer(this)
    }
}
