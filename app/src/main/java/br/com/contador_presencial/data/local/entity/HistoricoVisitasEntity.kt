package br.com.contador_presencial.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID
import kotlin.time.Duration

/**
 * Entidade Room que representa os registros de permanência no endereço.
 *
 * A referência para [EnderecoEntity] é opcional ([enderecoId] anulável) para permitir
 * registros independentes enquanto o fluxo de check-in ainda não estiver ativo, aplicando
 * exclusão em cascata quando vinculada a um endereço existente.
 */
@Entity(
    tableName = "historico_visitas",
    foreignKeys = [
        ForeignKey(
            entity = EnderecoEntity::class,
            parentColumns = ["id"],
            childColumns = ["endereco_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["endereco_id"])],
)
data class HistoricoVisitasEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "endereco_id")
    val enderecoId: UUID? = null,
    @ColumnInfo(name = "horas")
    val horas: Duration,
)
