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
    selectedMaterial: String,
    onMaterialSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MATERIAL_DISPLAY_NAMES.chunked(3).forEach { row ->
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
