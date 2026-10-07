package com.example.racunanjekilaze.data

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class OrderStorageTest {
    private val entry = OrderEntry(
        id = 7,
        dimensions = RollDimensions(700.0, 400.0, 1.5, 72.5),
        material = "CuZn30",
        coilCount = 3,
        result = CalculationResult(RollResult(129.59, 167.202), 3, 501.606)
    )

    @Test
    fun encodeThenDecodeKeepsEntries() {
        val other = entry.copy(id = 8, material = "Cu")

        assertThat(decodeOrder(encodeOrder(listOf(entry, other)))).containsExactly(entry, other).inOrder()
    }

    @Test
    fun decodeSkipsBrokenLines() {
        val text = encodeOrder(listOf(entry)) + "\nsmeće;1\n"

        assertThat(decodeOrder(text)).containsExactly(entry)
    }

    @Test
    fun emptyOrderRoundTrips() {
        assertThat(decodeOrder(encodeOrder(emptyList()))).isEmpty()
    }
}
