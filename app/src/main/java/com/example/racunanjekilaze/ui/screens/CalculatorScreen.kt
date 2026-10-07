package com.example.racunanjekilaze.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.data.CalculationResult
import com.example.racunanjekilaze.ui.components.PrimaryButton
import com.example.racunanjekilaze.ui.sections.HeaderSection
import com.example.racunanjekilaze.ui.sections.InputSection
import com.example.racunanjekilaze.ui.sections.KilazaResultCard
import com.example.racunanjekilaze.ui.theme.Accent
import com.example.racunanjekilaze.ui.theme.Background
import com.example.racunanjekilaze.ui.theme.CopperDark
import com.example.racunanjekilaze.ui.theme.ErrorColor
import com.example.racunanjekilaze.ui.theme.LayoutTokens
import com.example.racunanjekilaze.ui.theme.ResultGreen
import com.example.racunanjekilaze.ui.theme.SuccessColor
import com.example.racunanjekilaze.ui.theme.TextSecondary
import com.example.racunanjekilaze.ui.theme.layoutTokens
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

private const val PAGE_CALCULATOR = 0
private const val PAGE_ORDER = 1

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen() {
    val state = rememberCalculatorScreenState()
    val layout = layoutTokens()
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
        ) {
            HeaderSection(
                layout = layout,
                modifier = Modifier.padding(top = layout.screenPaddingVertical)
            )
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = Accent
            ) {
                Tab(
                    selected = pagerState.currentPage == PAGE_CALCULATOR,
                    onClick = { scope.launch { pagerState.animateScrollToPage(PAGE_CALCULATOR) } },
                    selectedContentColor = Accent,
                    unselectedContentColor = TextSecondary,
                    text = { Text("KALKULATOR", style = MaterialTheme.typography.labelLarge) }
                )
                Tab(
                    selected = pagerState.currentPage == PAGE_ORDER,
                    onClick = { scope.launch { pagerState.animateScrollToPage(PAGE_ORDER) } },
                    selectedContentColor = Accent,
                    unselectedContentColor = TextSecondary,
                    text = {
                        BadgedBox(
                            badge = {
                                if (state.orderEntries.isNotEmpty()) {
                                    Badge(
                                        modifier = Modifier.offset(x = 10.dp),
                                        containerColor = Accent,
                                        contentColor = Background
                                    ) {
                                        Text("${state.orderEntries.size}")
                                    }
                                }
                            }
                        ) {
                            Text("TRAKE", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                )
            }
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 1,
                modifier = Modifier.weight(1f)
            ) { page ->
                if (page == PAGE_CALCULATOR) {
                    CalculatorPage(state = state, layout = layout)
                } else {
                    OrderScreen(state = state, layout = layout)
                }
            }
        }
    }
}

@Composable
private fun CalculatorPage(
    state: CalculatorScreenState,
    layout: LayoutTokens
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val imeInsets = WindowInsets.ime

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
        keyboardController?.hide()
        focusManager.clearFocus()
        scrollRequest++
    }

    // Izmena unosa poništava stari rezultat, da se nikad ne vidi kilaža za druge dimenzije.
    val inputKey = listOf(
        state.radialThickness, state.coreDiameter, state.thickness,
        state.width, state.coils, state.selectedMaterial
    )
    LaunchedEffect(inputKey) {
        calculatedPreview = null
        message = ""
    }

    // Spusti ekran na rezultat tek kad se tastatura sakrije i raspored ustali: jedan skrol, bez jurenja visine.
    LaunchedEffect(scrollRequest) {
        if (scrollRequest == 0) return@LaunchedEffect
        withTimeoutOrNull(600) {
            snapshotFlow { imeInsets.getBottom(density) }.first { it == 0 }
        }
        repeat(2) { withFrameNanos { } }
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(
                start = layout.screenPaddingHorizontal,
                end = layout.screenPaddingHorizontal,
                top = layout.sectionSpacing
            ),
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

        if (message.isNotBlank()) {
            StatusMessage(text = message, isError = isError)
        }

        AnimatedVisibility(
            visible = calculatedPreview != null,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(120))
        ) {
            calculatedPreview?.let { preview ->
                Column(verticalArrangement = Arrangement.spacedBy(layout.sectionSpacing)) {
                    KilazaResultCard(
                        preview = preview,
                        layout = layout
                    )
                    PrimaryButton(
                        text = "DODAJ U TRAKE",
                        onClick = {
                            if (state.addCurrentToOrder()) {
                                isError = false
                                message = "✓ Dodato: ${state.orderEntries.size}"
                            } else {
                                isError = true
                                message = "Nije moguće dodati traku."
                            }
                        },
                        containerColor = CopperDark,
                        modifier = Modifier.fillMaxWidth(),
                        minHeight = layout.buttonMinHeight,
                        contentPadding = layout.buttonContentPadding
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .padding(bottom = layout.screenPaddingVertical)
        )
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
            .padding(horizontal = 14.dp, vertical = 8.dp),
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
