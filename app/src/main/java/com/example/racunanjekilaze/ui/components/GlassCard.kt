package com.example.racunanjekilaze.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.theme.CopperShadow
import com.example.racunanjekilaze.ui.theme.GlassBorder
import com.example.racunanjekilaze.ui.theme.Surface

/** Posvetli boju ka beloj za zadati faktor (0f = bez promene). */
private fun Color.lighten(factor: Float): Color = lerp(this, Color.White, factor)

/** Zatamni boju ka crnoj za zadati faktor (0f = bez promene). */
private fun Color.darken(factor: Float): Color = lerp(this, Color.Black, factor)

/**
 * Vertikalni gradijent koji daje kartici blagu dubinu:
 * vrh je za nijansu svetliji, dno za nijansu tamnije od osnovne boje.
 */
private fun cardBrush(base: Color): Brush = Brush.verticalGradient(
    colors = listOf(base.lighten(0.06f), base, base.darken(0.12f))
)

/**
 * Suptilna "staklena" ivica: svetlija na vrhu (odsjaj), skoro nevidljiva pri dnu.
 */
private fun borderBrush(color: Color): Brush = Brush.verticalGradient(
    colors = listOf(color.copy(alpha = 0.45f), color.copy(alpha = 0.12f))
)

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    bgColor: Color = Surface,
    borderColor: Color = GlassBorder,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 10.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = CopperShadow,
                spotColor = CopperShadow
            )
            .clip(shape)
            .background(cardBrush(bgColor.copy(alpha = 0.97f)))
            .border(
                width = 1.dp,
                brush = borderBrush(borderColor),
                shape = shape
            ),
        content = content
    )
}

@Composable
fun AccentGlassCard(
    modifier: Modifier = Modifier,
    bgColor: Color,
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 12.dp,
    borderColor: Color = Color.White,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = CopperShadow,
                spotColor = CopperShadow
            )
            .clip(shape)
            .background(cardBrush(bgColor))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        borderColor.copy(alpha = 0.18f),
                        borderColor.copy(alpha = 0.04f)
                    )
                ),
                shape = shape
            ),
        content = content
    )
}
