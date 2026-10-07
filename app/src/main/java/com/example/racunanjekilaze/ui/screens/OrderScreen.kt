package com.example.racunanjekilaze.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.sections.OrderEntryRow
import com.example.racunanjekilaze.ui.sections.TotalSection
import com.example.racunanjekilaze.ui.theme.ErrorColor
import com.example.racunanjekilaze.ui.theme.LayoutTokens
import com.example.racunanjekilaze.ui.theme.TextSecondary

/** Drugo polje: zapamćene trake i njihova ukupna kilaža. */
@Composable
fun OrderScreen(
    state: CalculatorScreenState,
    layout: LayoutTokens,
    modifier: Modifier = Modifier
) {
    var confirmClear by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = layout.screenPaddingHorizontal,
            end = layout.screenPaddingHorizontal,
            top = layout.sectionSpacing,
            bottom = layout.screenPaddingVertical
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "total") {
            TotalSection(
                totalWeightKg = state.totalWeight,
                totalCoilsText = state.totalCoilsText,
                remainingWeight = null,
                layout = layout
            )
        }
        if (state.orderEntries.isEmpty()) {
            item(key = "empty") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 96.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nema zapamćenih traka",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            itemsIndexed(
                items = state.orderEntries,
                key = { _, entry -> entry.id }
            ) { index, entry ->
                OrderEntryRow(
                    entry = entry,
                    index = index + 1,
                    onDelete = { state.removeEntry(entry.id) },
                    layout = layout
                )
            }
            item(key = "clear") {
                OutlinedButton(
                    onClick = { confirmClear = true },
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorColor)
                ) {
                    Text("OBRIŠI SVE", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Obrisati sve trake?") },
            confirmButton = {
                TextButton(onClick = {
                    state.clearOrder()
                    confirmClear = false
                }) {
                    Text("Obriši", color = ErrorColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) {
                    Text("Otkaži")
                }
            }
        )
    }
}
