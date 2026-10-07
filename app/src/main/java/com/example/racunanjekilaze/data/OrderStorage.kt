package com.example.racunanjekilaze.data

import android.content.Context

private const val PREFS_NAME = "kilaza_nalog"
private const val KEY_ORDER = "stavke"
private const val FIELD_SEPARATOR = ';'

/** Pretvara stavke naloga u tekst, jedna stavka po redu (decimale uvek sa tačkom). */
fun encodeOrder(entries: List<OrderEntry>): String {
    return entries.joinToString("\n") { entry ->
        listOf(
            entry.id,
            entry.dimensions.outerDiameterMm,
            entry.dimensions.coreDiameterMm,
            entry.dimensions.thicknessMm,
            entry.dimensions.widthMm,
            entry.material,
            entry.coilCount,
            entry.result.singleRoll.lengthM,
            entry.result.singleRoll.weightKg,
            entry.result.coilCount,
            entry.result.totalWeightKg
        ).joinToString(FIELD_SEPARATOR.toString())
    }
}

/** Vraća stavke iz teksta; neispravan red se preskače umesto da sruši aplikaciju. */
fun decodeOrder(text: String): List<OrderEntry> {
    return text.lineSequence()
        .filter { it.isNotBlank() }
        .mapNotNull { line ->
            runCatching {
                val f = line.split(FIELD_SEPARATOR)
                OrderEntry(
                    id = f[0].toInt(),
                    dimensions = RollDimensions(
                        outerDiameterMm = f[1].toDouble(),
                        coreDiameterMm = f[2].toDouble(),
                        thicknessMm = f[3].toDouble(),
                        widthMm = f[4].toDouble()
                    ),
                    material = f[5],
                    coilCount = f[6].toInt(),
                    result = CalculationResult(
                        singleRoll = RollResult(f[7].toDouble(), f[8].toDouble()),
                        coilCount = f[9].toInt(),
                        totalWeightKg = f[10].toDouble()
                    )
                )
            }.getOrNull()
        }
        .toList()
}

fun loadOrder(context: Context): List<OrderEntry> {
    val text = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getString(KEY_ORDER, null) ?: return emptyList()
    return decodeOrder(text)
}

fun saveOrder(context: Context, entries: List<OrderEntry>) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putString(KEY_ORDER, encodeOrder(entries))
        .apply()
}
