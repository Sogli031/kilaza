package com.example.racunanjekilaze.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.theme.MATERIAL_DISPLAY_NAMES
import com.example.racunanjekilaze.ui.theme.TextSecondary

@Composable
fun MaterialGridSelector(
    label: String,
    selectedMaterial: String,
    onMaterialSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val materials = MATERIAL_DISPLAY_NAMES
    val rows = materials.chunked(3)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { material ->
                        SelectableChip(
                            text = material,
                            isSelected = material == selectedMaterial,
                            onClick = { onMaterialSelected(material) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
