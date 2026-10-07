package com.example.racunanjekilaze.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.theme.Accent
import com.example.racunanjekilaze.ui.theme.BorderColor
import com.example.racunanjekilaze.ui.theme.Copper
import com.example.racunanjekilaze.ui.theme.SurfaceElevated
import com.example.racunanjekilaze.ui.theme.SurfaceLight
import com.example.racunanjekilaze.ui.theme.TextPrimary

/** Zajednička pločica za izbor (materijal, prečnik doboša) na bazi Material 3 FilterChip-a. */
@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    minHeight: Dp = 44.dp
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = SurfaceLight,
            labelColor = TextPrimary,
            selectedContainerColor = SurfaceElevated,
            selectedLabelColor = Accent
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = BorderColor,
            selectedBorderColor = Copper,
            borderWidth = 1.dp,
            selectedBorderWidth = 2.dp
        ),
        modifier = modifier
            .height(minHeight)
            .semantics { stateDescription = if (isSelected) "Izabran" else "Nije izabran" }
    )
}
