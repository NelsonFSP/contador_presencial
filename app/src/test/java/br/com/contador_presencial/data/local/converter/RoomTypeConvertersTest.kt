package br.com.contador_presencial.data.local.converter

import br.com.contador_presencial.domain.model.Periodicidade
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.UUID
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class RoomTypeConvertersTest {
    private val converters = RoomTypeConverters()

    @Test
    fun uuidRoundTripPreservesValueAndNull() {
        val uuid = UUID.randomUUID()
        val serialized = converters.fromUuid(uuid)
        assertEquals(uuid.toString(), serialized)
        assertEquals(uuid, converters.toUuid(serialized))

        assertNull(converters.fromUuid(null))
        assertNull(converters.toUuid(null))
    }

    @Test
    fun periodicidadeRoundTripSupportsEnumAndCaseInsensitiveValues() {
        assertEquals("SEMANAL", converters.fromPeriodicidade(Periodicidade.SEMANAL))
        assertEquals("MENSAL", converters.fromPeriodicidade(Periodicidade.MENSAL))
        assertEquals(Periodicidade.SEMANAL, converters.toPeriodicidade("SEMANAL"))
        assertEquals(Periodicidade.MENSAL, converters.toPeriodicidade("mensal"))
        assertNull(converters.fromPeriodicidade(null))
        assertNull(converters.toPeriodicidade(null))
    }

    @Test(expected = IllegalArgumentException::class)
    fun toPeriodicidadeThrowsForUnknownValue() {
        converters.toPeriodicidade("ANUAL")
    }

    @Test
    fun durationRoundTripPreservesMillisecondsAndNull() {
        val duration = 8.hours + 30.minutes
        val millis = converters.fromDuration(duration)
        assertEquals(duration.inWholeMilliseconds, millis)
        assertEquals(duration.inWholeMilliseconds, converters.toDuration(millis)?.inWholeMilliseconds)

        assertNull(converters.fromDuration(null))
        assertNull(converters.toDuration(null))
    }
}
