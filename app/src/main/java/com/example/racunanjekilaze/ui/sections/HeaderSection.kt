package com.example.racunanjekilaze.ui.sections

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.R
import com.example.racunanjekilaze.ui.components.AutoSizeText
import com.example.racunanjekilaze.ui.theme.LayoutTokens
import com.example.racunanjekilaze.ui.theme.TextPrimary

/** Kompaktno zaglavlje: logo i naslov u jednom redu. */
@Composable
fun HeaderSection(
    layout: LayoutTokens,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = layout.screenPaddingHorizontal),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(if (layout.isCompact) 36.dp else 44.dp)
        )
        AutoSizeText(
            text = "Računanje kilaže",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            maxFontSize = MaterialTheme.typography.titleLarge.fontSize,
            maxLines = 1
        )
    }
}
