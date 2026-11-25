package br.com.contador_presencial.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.contador_presencial.data.local.converter.RoomTypeConverters
import br.com.contador_presencial.data.local.dao.AutorDao
import br.com.contador_presencial.data.local.dao.EnderecoDao
import br.com.contador_presencial.data.local.dao.HistoricoVisitasDao
import br.com.contador_presencial.data.local.entity.AutorEntity
import br.com.contador_presencial.data.local.entity.EnderecoEntity
import br.com.contador_presencial.data.local.entity.HistoricoVisitasEntity

/**
 * Banco de dados local Room do aplicativo Contador Presencial.
 *
 * Controle de versionamento e migrações:
 * - Sempre incremente [DATABASE_VERSION] ao alterar entidades, colunas ou índices.
 * - Mantenha `exportSchema = true` para gerar o histórico em `app/schemas`.
 * - Registre objetos `Migration(from, to)` explícitos ou `@AutoMigration` no builder em
 *   [buildInstance] para preservar dados existentes sem perda entre versões.
 */
@Database(
    entities = [
        AutorEntity::class,
        EnderecoEntity::class,
        HistoricoVisitasEntity::class,
    ],
    version = AppDatabase.DATABASE_VERSION,
    exportSchema = true,
)
@TypeConverters(RoomTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun autorDao(): AutorDao

    abstract fun enderecoDao(): EnderecoDao

    abstract fun historicoVisitasDao(): HistoricoVisitasDao

    companion object {
        const val DATABASE_NAME = "contador_presencial.db"
        const val DATABASE_VERSION = 1

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildInstance(context.applicationContext).also { instance = it }
            }

        private fun buildInstance(appContext: Context): AppDatabase =
            Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                DATABASE_NAME,
            ).build()
    }
}
