package br.com.contador_presencial.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.contador_presencial.domain.model.Periodicidade
import java.util.UUID

/**
 * Entidade Room que representa o autor/perfil e sua periodicidade de contagem.
 */
@Entity(tableName = "autor")
data class AutorEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "periodicidade")
    val periodicidade: Periodicidade,
)
