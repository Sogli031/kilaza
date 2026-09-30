package com.example.racunanjekilaze.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.theme.Accent
import com.example.racunanjekilaze.ui.theme.BorderColor
import com.example.racunanjekilaze.ui.theme.Copper
import com.example.racunanjekilaze.ui.theme.SurfaceElevated
import com.example.racunanjekilaze.ui.theme.SurfaceLight
import com.example.racunanjekilaze.ui.theme.TextPrimary

private const val CHIP_ANIMATION_MS = 180

/**
 * Zajednička pločica za izbor (materijal, prečnik doboša).
 * Izabrano stanje ima bakarni okvir i blagi gradijent u pozadini.
 */
@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    minHeight: androidx.compose.ui.unit.Dp = 52.dp
) {
    val shape = RoundedCornerShape(14.dp)
    val animationSpec = tween<androidx.compose.ui.graphics.Color>(CHIP_ANIMATION_MS)

    val topColor by animateColorAsState(
        targetValue = if (isSelected) SurfaceElevated else SurfaceLight,
        animationSpec = animationSpec,
        label = "chipTop"
    )
    val bottomColor by animateColorAsState(
        targetValue = if (isSelected) Copper.copy(alpha = 0.22f) else SurfaceLight,
        animationSpec = animationSpec,
        label = "chipBottom"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Copper else BorderColor,
        animationSpec = animationSpec,
        label = "chipBorder"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Accent else TextPrimary,
        animationSpec = animationSpec,
        label = "chipText"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        animationSpec = tween(CHIP_ANIMATION_MS),
        label = "chipBorderWidth"
    )

    Box(
        modifier = modifier
            .heightIn(min = minHeight)
            .clip(shape)
            .background(Brush.verticalGradient(colors = listOf(topColor, bottomColor)))
            .border(borderWidth, borderColor, shape)
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                onClick = onClick
            )
            .semantics {
                stateDescription = if (isSelected) "Izabran" else "Nije izabran"
            }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
