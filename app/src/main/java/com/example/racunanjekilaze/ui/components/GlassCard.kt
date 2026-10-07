package com.example.racunanjekilaze.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.theme.DividerColor
import com.example.racunanjekilaze.ui.theme.Surface

/** Tonska Material 3 kartica sa tankom ivicom. */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    bgColor: Color = Surface,
    borderColor: Color = DividerColor,
    cornerRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Box(content = content)
    }
}

/** Kartica sa zadatom bojom pozadine (rezultat, ukupno, stavke). */
@Composable
fun AccentGlassCard(
    modifier: Modifier = Modifier,
    bgColor: Color,
    cornerRadius: Dp = 20.dp,
    borderColor: Color = Color.White,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor.copy(alpha = 0.25f))
    ) {
        Box(content = content)
    }
}
