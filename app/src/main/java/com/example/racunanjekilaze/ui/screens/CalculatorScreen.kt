package com.example.racunanjekilaze.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.withTimeoutOrNull
import com.example.racunanjekilaze.data.CalculationResult
import com.example.racunanjekilaze.ui.components.PrimaryButton
import com.example.racunanjekilaze.ui.sections.HeaderSection
import com.example.racunanjekilaze.ui.sections.InputSection
import com.example.racunanjekilaze.ui.sections.KilazaResultCard
import com.example.racunanjekilaze.ui.theme.BackgroundGradientEnd
import com.example.racunanjekilaze.ui.theme.BackgroundGradientMid
import com.example.racunanjekilaze.ui.theme.BackgroundGradientStart
import com.example.racunanjekilaze.ui.theme.CopperGlow
import com.example.racunanjekilaze.ui.theme.ErrorColor
import com.example.racunanjekilaze.ui.theme.ResultGreen
import com.example.racunanjekilaze.ui.theme.SuccessColor
import com.example.racunanjekilaze.ui.theme.TealGlow
import com.example.racunanjekilaze.ui.theme.layoutTokens

@Composable
fun CalculatorScreen() {
    val state = rememberCalculatorScreenState()
    val layout = layoutTokens()
    val scrollState = rememberScrollState()

    val radialFocus = remember { FocusRequester() }
    val coreFocus = remember { FocusRequester() }
    val thicknessFocus = remember { FocusRequester() }
    val widthFocus = remember { FocusRequester() }
    val coilsFocus = remember { FocusRequester() }
    var calculatedPreview by remember { mutableStateOf<CalculationResult?>(null) }
    var message by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var scrollRequest by remember { mutableIntStateOf(0) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val doCalculate: () -> Unit = {
        val preview = state.currentPreview
        if (preview == null) {
            calculatedPreview = null
            isError = true
            message = "Popuni sva obavezna polja ispravno."
        } else {
            calculatedPreview = preview
            isError = false
            message = "✓ Proračun uspešno završen."
        }
        // Skloni tastaturu i spusti ekran na rezultat.
        keyboardController?.hide()
        focusManager.clearFocus()
        scrollRequest++
    }

    // Rezultat se pojavljuje uz animaciju, pa pratimo dno dok se visina menja.
    LaunchedEffect(scrollRequest) {
        if (scrollRequest == 0) return@LaunchedEffect
        withTimeoutOrNull(700) {
            snapshotFlow { scrollState.maxValue }.collect { maxValue ->
                scrollState.animateScrollTo(maxValue)
            }
        }
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            BackgroundGradientStart,
            BackgroundGradientMid,
            BackgroundGradientEnd
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        // Blagi bakarni sjaj u gornjem delu i hladniji odsjaj pri dnu ekrana.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(CopperGlow, Color.Transparent),
                        radius = 900f
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, TealGlow)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .statusBarsPadding()
                .padding(bottom = layout.screenPaddingVertical)
        ) {
            HeaderSection(
                layout = layout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = layout.screenPaddingVertical)
            )
            Spacer(modifier = Modifier.height(layout.sectionSpacing))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = layout.screenPaddingHorizontal),
                verticalArrangement = Arrangement.spacedBy(layout.sectionSpacing)
            ) {
                InputSection(
                    radialThickness = state.radialThickness,
                    onRadialThicknessChange = { state.radialThickness = it },
                    coreDiameter = state.coreDiameter,
                    onCoreDiameterChange = { state.coreDiameter = it },
                    thickness = state.thickness,
                    onThicknessChange = { state.thickness = it },
                    width = state.width,
                    onWidthChange = { state.width = it },
                    selectedMaterial = state.selectedMaterial,
                    onMaterialSelected = { state.selectedMaterial = it },
                    coils = state.coils,
                    onCoilsChange = { state.coils = it },
                    layout = layout,
                    radialFocus = radialFocus,
                    coreFocus = coreFocus,
                    thicknessFocus = thicknessFocus,
                    widthFocus = widthFocus,
                    coilsFocus = coilsFocus,
                    onCalculate = doCalculate
                )

                PrimaryButton(
                    text = "IZRAČUNAJ",
                    onClick = doCalculate,
                    containerColor = ResultGreen,
                    modifier = Modifier.fillMaxWidth(),
                    minHeight = layout.buttonMinHeight,
                    contentPadding = layout.buttonContentPadding
                )

                AnimatedVisibility(
                    visible = message.isNotBlank(),
                    enter = fadeIn(tween(200)) + expandVertically(tween(200)),
                    exit = fadeOut(tween(150)) + shrinkVertically(tween(150))
                ) {
                    StatusMessage(text = message, isError = isError)
                }

                AnimatedVisibility(
                    visible = calculatedPreview != null,
                    enter = fadeIn(tween(250)) +
                        expandVertically(tween(250)) +
                        scaleIn(tween(250), initialScale = 0.94f),
                    exit = fadeOut(tween(150)) + shrinkVertically(tween(150))
                ) {
                    calculatedPreview?.let { preview ->
                        KilazaResultCard(
                            preview = preview,
                            layout = layout
                        )
                    }
                }
            }
        }
    }
}

/** Poruka o statusu proračuna prikazana kao mala obojena "pilula". */
@Composable
private fun StatusMessage(
    text: String,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val accent = if (isError) ErrorColor else SuccessColor

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(accent.copy(alpha = 0.12f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accent)
        )
        Text(
            text = text,
            color = accent,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}
