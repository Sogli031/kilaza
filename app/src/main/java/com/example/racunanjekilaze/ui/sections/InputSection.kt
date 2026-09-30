package com.example.racunanjekilaze.ui.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.racunanjekilaze.data.CalculationResult
import com.example.racunanjekilaze.data.formatLength
import com.example.racunanjekilaze.data.formatValue
import com.example.racunanjekilaze.data.getTrakaForm
import com.example.racunanjekilaze.ui.components.AccentGlassCard
import com.example.racunanjekilaze.ui.components.AutoSizeText
import com.example.racunanjekilaze.ui.components.GlassCard
import com.example.racunanjekilaze.ui.components.LabeledTextField
import com.example.racunanjekilaze.ui.components.MaterialGridSelector
import com.example.racunanjekilaze.ui.components.SectionHeader
import com.example.racunanjekilaze.ui.components.SelectableChip
import com.example.racunanjekilaze.ui.components.SoftDivider
import com.example.racunanjekilaze.ui.components.StatTile
import com.example.racunanjekilaze.ui.components.fieldColors
import com.example.racunanjekilaze.ui.theme.LayoutTokens
import com.example.racunanjekilaze.ui.theme.ResultAccent
import com.example.racunanjekilaze.ui.theme.ResultGreen
import com.example.racunanjekilaze.ui.theme.ResultGreenDeep
import com.example.racunanjekilaze.ui.theme.TextPrimary
import com.example.racunanjekilaze.ui.theme.TextSecondary
import java.util.Locale

private val CORE_DIAMETER_PRESETS = listOf(
    "400" to "400 mm",
    "500" to "500 mm"
)

internal fun formatCoilWeightTitle(coilCount: Int): String {
    return "Kilaža za $coilCount ${getTrakaForm(coilCount)}"
}

@Composable
fun InputSection(
    radialThickness: String,
    onRadialThicknessChange: (String) -> Unit,
    coreDiameter: String,
    onCoreDiameterChange: (String) -> Unit,
    thickness: String,
    onThicknessChange: (String) -> Unit,
    width: String,
    onWidthChange: (String) -> Unit,
    selectedMaterial: String,
    onMaterialSelected: (String) -> Unit,
    coils: String,
    onCoilsChange: (String) -> Unit,
    layout: LayoutTokens,
    modifier: Modifier = Modifier,
    radialFocus: FocusRequester? = null,
    coreFocus: FocusRequester? = null,
    thicknessFocus: FocusRequester? = null,
    widthFocus: FocusRequester? = null,
    coilsFocus: FocusRequester? = null,
    onCalculate: () -> Unit = {}
) {
    val isRadialError = radialThickness.isNotBlank() && !radialThickness.isPositiveDecimal()
    val isCoreError = coreDiameter.isNotBlank() && !coreDiameter.isPositiveDecimal()
    val isThicknessError = thickness.isNotBlank() && !thickness.isPositiveDecimal()
    val isWidthError = width.isNotBlank() && !width.isPositiveDecimal()
    val isCoilsError = coils.isNotBlank() && coils.trim().toIntOrNull()?.let { it > 0 } != true

    GlassCard(
        modifier = modifier,
        cornerRadius = layout.cardRadius
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = layout.cardPaddingHorizontal,
                vertical = layout.cardPaddingVertical
            ),
            verticalArrangement = Arrangement.spacedBy(layout.fieldSpacing)
        ) {
            SectionHeader(title = "DIMENZIJE TRAKE")

            MaterialGridSelector(
                label = "Materijal",
                selectedMaterial = selectedMaterial,
                onMaterialSelected = { material ->
                    onMaterialSelected(material)
                    radialFocus?.requestFocus()
                }
            )

            SoftDivider()

            LabeledTextField(
                label = "Poluprečnik trake (mm)",
                value = radialThickness,
                onValueChange = onRadialThicknessChange,
                placeholder = "npr. 150",
                isError = isRadialError,
                keyboardType = KeyboardType.Decimal,
                focusRequester = radialFocus,
                imeAction = ImeAction.Next,
                onImeAction = { coreFocus?.requestFocus() }
            )

            CoreDiameterSelector(
                coreDiameter = coreDiameter,
                onCoreDiameterChange = onCoreDiameterChange,
                isError = isCoreError,
                focusRequester = coreFocus,
                nextFocus = thicknessFocus
            )

            LabeledTextField(
                label = "Debljina materijala (mm)",
                value = thickness,
                onValueChange = onThicknessChange,
                placeholder = "npr. 2",
                isError = isThicknessError,
                keyboardType = KeyboardType.Decimal,
                focusRequester = thicknessFocus,
                imeAction = ImeAction.Next,
                onImeAction = { widthFocus?.requestFocus() }
            )

            LabeledTextField(
                label = "Širina trake (mm)",
                value = width,
                onValueChange = onWidthChange,
                placeholder = "npr. 72",
                isError = isWidthError,
                keyboardType = KeyboardType.Decimal,
                focusRequester = widthFocus,
                imeAction = ImeAction.Next,
                onImeAction = { coilsFocus?.requestFocus() }
            )

            LabeledTextField(
                label = "Broj traka",
                value = coils,
                onValueChange = onCoilsChange,
                placeholder = "prazno = 1",
                isError = isCoilsError,
                keyboardType = KeyboardType.Number,
                focusRequester = coilsFocus,
                imeAction = ImeAction.Done,
                onImeAction = onCalculate
            )
        }
    }
}

@Composable
private fun CoreDiameterSelector(
    coreDiameter: String,
    onCoreDiameterChange: (String) -> Unit,
    isError: Boolean,
    focusRequester: FocusRequester?,
    nextFocus: FocusRequester?
) {
    val trimmedValue = coreDiameter.trim()
    val selectedPreset = CORE_DIAMETER_PRESETS.firstOrNull { it.first == trimmedValue }
    val customValue = if (selectedPreset == null) coreDiameter else ""

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Unutrašnji prečnik (mm)",
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CORE_DIAMETER_PRESETS.forEach { (value, label) ->
                SelectableChip(
                    text = label,
                    isSelected = selectedPreset?.first == value,
                    onClick = {
                        onCoreDiameterChange(value)
                        nextFocus?.requestFocus()
                    },
                    minHeight = 56.dp,
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = customValue,
                onValueChange = onCoreDiameterChange,
                placeholder = {
                    Text(
                        text = "Proizvoljno",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { nextFocus?.requestFocus() }
                ),
                isError = isError,
                colors = fieldColors(isError = isError),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 56.dp)
                    .then(
                        if (focusRequester != null) Modifier.focusRequester(focusRequester)
                        else Modifier
                    )
            )
        }
    }
}

/**
 * Zeleni panel sa rezultatom: velika ukupna kilaža, a ispod nje metraža trake
 * (za jednu traku i ukupno, kada ih ima više).
 */
@Composable
fun KilazaResultCard(
    preview: CalculationResult,
    layout: LayoutTokens,
    modifier: Modifier = Modifier
) {
    val hasMultipleCoils = preview.coilCount > 1

    AccentGlassCard(
        modifier = modifier,
        bgColor = ResultGreen,
        cornerRadius = layout.cardRadius,
        borderColor = ResultAccent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, ResultGreenDeep.copy(alpha = 0.55f))
                    )
                )
                .padding(
                    horizontal = layout.cardPaddingHorizontal,
                    vertical = layout.cardPaddingVertical
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = formatCoilWeightTitle(preview.coilCount).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.labelSmall,
                color = ResultAccent.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            AutoSizeText(
                text = "${formatValue(preview.totalWeightKg)} kg",
                style = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
                color = ResultAccent,
                maxFontSize = MaterialTheme.typography.displayLarge.fontSize,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if (hasMultipleCoils) {
                Text(
                    text = "Jedna traka: ${formatValue(preview.singleRoll.weightKg)} kg",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            SoftDivider(
                modifier = Modifier.padding(vertical = 6.dp),
                color = ResultAccent.copy(alpha = 0.35f)
            )

            StatTile(
                label = if (hasMultipleCoils) "METRAŽA (1 TRAKA)" else "METRAŽA TRAKE",
                value = formatLength(preview.singleRoll.lengthM),
                valueColor = TextPrimary,
                accentColor = ResultAccent,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun String.isPositiveDecimal(): Boolean {
    return trim().replace(',', '.').toDoubleOrNull()?.let { it > 0 } == true
}
