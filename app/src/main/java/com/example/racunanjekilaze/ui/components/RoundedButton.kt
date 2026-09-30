package com.example.racunanjekilaze.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.theme.CopperDark
import com.example.racunanjekilaze.ui.theme.CopperShadow
import com.example.racunanjekilaze.ui.theme.SurfaceLight
import com.example.racunanjekilaze.ui.theme.TextSecondary

/**
 * Glavno dugme sa gradijentom izvedenim iz zadate boje
 * i blagim "utiskivanjem" pri pritisku.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = CopperDark,
    minHeight: Dp = 58.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
) {
    val shape = MaterialTheme.shapes.large
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(),
        label = "buttonScale"
    )

    val gradient = if (enabled) {
        Brush.horizontalGradient(
            colors = listOf(
                lerp(containerColor, Color.White, 0.14f),
                containerColor,
                lerp(containerColor, Color.Black, 0.16f)
            )
        )
    } else {
        Brush.horizontalGradient(colors = listOf(SurfaceLight, SurfaceLight))
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .scale(scale)
            .heightIn(min = minHeight)
            .shadow(
                elevation = if (enabled) 10.dp else 0.dp,
                shape = shape,
                ambientColor = CopperShadow,
                spotColor = CopperShadow
            )
            .clip(shape)
            .background(gradient),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = TextSecondary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minHeight: Dp = 54.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = minHeight),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceLight,
            contentColor = TextSecondary,
            disabledContainerColor = SurfaceLight,
            disabledContentColor = TextSecondary
        ),
        contentPadding = contentPadding
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SmallButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
