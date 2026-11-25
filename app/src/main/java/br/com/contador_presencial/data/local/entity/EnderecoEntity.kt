package br.com.contador_presencial.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Entidade Room que representa o endereço de trabalho presencial associado ao autor.
 */
@Entity(
    tableName = "endereco",
    foreignKeys = [
        ForeignKey(
            entity = AutorEntity::class,
            parentColumns = ["id"],
            childColumns = ["autor_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["autor_id"])],
)
data class EnderecoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "autor_id")
    val autorId: UUID? = null,
    @ColumnInfo(name = "rua")
    val rua: String,
    @ColumnInfo(name = "numero")
    val numero: Int,
    @ColumnInfo(name = "complemento")
    val complemento: String? = null,
    @ColumnInfo(name = "bairro")
    val bairro: String,
    @ColumnInfo(name = "cidade")
    val cidade: String,
    @ColumnInfo(name = "uf")
    val uf: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double? = null,
    @ColumnInfo(name = "longitude")
    val longitude: Double? = null,
)
