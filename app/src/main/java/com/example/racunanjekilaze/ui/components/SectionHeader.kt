package com.example.racunanjekilaze.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.ui.theme.Accent
import com.example.racunanjekilaze.ui.theme.Copper
import com.example.racunanjekilaze.ui.theme.DividerColor
import com.example.racunanjekilaze.ui.theme.TextPrimary
import com.example.racunanjekilaze.ui.theme.TextSecondary

/**
 * Naslov sekcije sa bakarnom vertikalnom crticom sa leve strane
 * i opcionim tekstom (npr. brojač) sa desne.
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Accent, Copper)
                    )
                )
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (trailingText != null) {
            Text(
                text = trailingText,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Tanka linija za razdvajanje sadržaja unutar kartice. */
@Composable
fun SoftDivider(
    modifier: Modifier = Modifier,
    color: Color = DividerColor
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        color.copy(alpha = 0.9f),
                        Color.Transparent
                    )
                )
            )
    )
}

/**
 * Mala pločica sa oznakom i vrednošću — koristi se za prikaz
 * metraže i drugih sporednih podataka ispod glavnog rezultata.
 */
@Composable
fun StatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = TextPrimary,
    accentColor: Color = Accent
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.18f))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(5.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.8f))
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        AutoSizeText(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(textAlign = TextAlign.Center),
            color = valueColor,
            maxFontSize = MaterialTheme.typography.titleSmall.fontSize,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
