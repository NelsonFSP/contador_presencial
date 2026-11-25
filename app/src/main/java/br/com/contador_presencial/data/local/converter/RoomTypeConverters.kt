package br.com.contador_presencial.data.local.converter

import androidx.room.TypeConverter
import br.com.contador_presencial.domain.model.Periodicidade
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Conversores de tipos customizados para persistência no Room.
 */
class RoomTypeConverters {
    @TypeConverter
    fun fromUuid(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toUuid(value: String?): UUID? = value?.let(UUID::fromString)

    @TypeConverter
    fun fromPeriodicidade(periodicidade: Periodicidade?): String? = periodicidade?.name

    @TypeConverter
    fun toPeriodicidade(value: String?): Periodicidade? =
        value?.let { raw ->
            Periodicidade.entries.firstOrNull { it.name.equals(raw, ignoreCase = true) }
                ?: throw IllegalArgumentException("Periodicidade desconhecida: $raw")
        }

    @TypeConverter
    fun fromDuration(duration: Duration?): Long? = duration?.inWholeMilliseconds

    @TypeConverter
    fun toDuration(millis: Long?): Duration? = millis?.milliseconds
}
