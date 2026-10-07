package com.example.racunanjekilaze.ui.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.data.OrderEntry
import com.example.racunanjekilaze.data.formatLength
import com.example.racunanjekilaze.data.formatValue
import com.example.racunanjekilaze.ui.components.AccentGlassCard
import com.example.racunanjekilaze.ui.theme.Accent
import com.example.racunanjekilaze.ui.theme.ErrorColor
import com.example.racunanjekilaze.ui.theme.LayoutTokens
import com.example.racunanjekilaze.ui.theme.SuccessColor
import com.example.racunanjekilaze.ui.theme.SurfaceLight
import com.example.racunanjekilaze.ui.theme.TextPrimary
import com.example.racunanjekilaze.ui.theme.TextSecondary
import java.util.Locale

/** Jedna traka iz naloga: dimenzije, broj komada, metraža i kilaža. */
@Composable
fun OrderEntryRow(
    entry: OrderEntry,
    index: Int,
    onDelete: () -> Unit,
    layout: LayoutTokens,
    modifier: Modifier = Modifier
) {
    val dimensions = entry.dimensions
    val thicknessText = String.format(Locale.getDefault(), "%.2g", dimensions.thicknessMm)
    val primaryText = "${entry.material}  •  $thicknessText x ${formatValue(dimensions.widthMm, 0)} mm"
    val secondaryText = "${entry.coilCount} kom  •  ${formatLength(entry.result.singleRoll.lengthM)}"

    AccentGlassCard(
        modifier = modifier,
        bgColor = SurfaceLight,
        cornerRadius = 14.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = layout.itemPaddingHorizontal,
                    top = layout.itemPaddingVertical,
                    bottom = layout.itemPaddingVertical,
                    end = 2.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = primaryText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = secondaryText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${formatValue(entry.result.totalWeightKg)} kg",
                    style = MaterialTheme.typography.titleSmall,
                    color = Accent,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                if (entry.coilCount > 1) {
                    Text(
                        text = "${formatValue(entry.result.singleRoll.weightKg)} kg / traka",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessColor,
                        maxLines = 1
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Obriši stavku",
                    tint = ErrorColor
                )
            }
        }
    }
}
