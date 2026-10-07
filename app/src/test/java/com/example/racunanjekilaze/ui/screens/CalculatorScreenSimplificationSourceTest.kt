package com.example.racunanjekilaze.ui.screens

import com.google.common.truth.Truth.assertThat
import java.nio.file.Path
import org.junit.Test

class CalculatorScreenSimplificationSourceTest {
    @Test
    fun calculatorScreenHasCalculatorAndSavedStripsTabs() {
        val screen = readSource("src/main/java/com/example/racunanjekilaze/ui/screens/CalculatorScreen.kt")

        assertThat(screen).contains("PrimaryTabRow(")
        assertThat(screen).contains("\"KALKULATOR\"")
        assertThat(screen).contains("\"TRAKE\"")
        assertThat(screen).contains("\"DODAJ U TRAKE\"")
        assertThat(screen).contains("OrderScreen(")
        assertThat(screen).doesNotContain("AlertDialog")
    }

    @Test
    fun inputSectionShowsCoreDiameterPresetsAndCustomEntry() {
        val input = readSource("src/main/java/com/example/racunanjekilaze/ui/sections/InputSection.kt")

        assertThat(input).contains("CoreDiameterSelector(")
        assertThat(input).contains("\"400 mm\"")
        assertThat(input).contains("\"500 mm\"")
        assertThat(input).contains("\"Proizvoljno\"")
        assertThat(input).contains("\"Unutrašnji prečnik (mm)\"")
    }

    @Test
    fun resultCopyOnlyNamesEnteredCountAndSingleStripWeight() {
        val input = readSource("src/main/java/com/example/racunanjekilaze/ui/sections/InputSection.kt")

        assertThat(input).contains("formatCoilWeightTitle(preview.coilCount)")
        assertThat(input).contains("\"Jedna traka:")
        assertThat(input).doesNotContain("Kilaža za broj traka")
        assertThat(input).doesNotContain("PREGLED PRE DODAVANJA")
        assertThat(input).doesNotContain("Ukupno za unos:")
    }

    @Test
    fun resultShowsStripLengthNextToWeight() {
        val input = readSource("src/main/java/com/example/racunanjekilaze/ui/sections/InputSection.kt")

        assertThat(input).contains("METRAŽA")
        assertThat(input).contains("formatLength(preview.singleRoll.lengthM)")
        assertThat(input).doesNotContain("UKUPNA METRAŽA")
    }

    @Test
    fun calculatorScreenUsesPrecnikStyleCalculateFlow() {
        val screen = readSource("src/main/java/com/example/racunanjekilaze/ui/screens/CalculatorScreen.kt")

        assertThat(screen).contains("PrimaryButton(")
        assertThat(screen).contains("\"IZRAČUNAJ\"")
        assertThat(screen).contains("\"✓ Proračun uspešno završen.\"")
        assertThat(screen).contains("KilazaResultCard(")
        assertThat(screen).contains("ResultGreen")
    }

    @Test
    fun resultSummaryIsCenteredInDedicatedGreenBox() {
        val input = readSource("src/main/java/com/example/racunanjekilaze/ui/sections/InputSection.kt")

        assertThat(input).contains("KilazaResultCard(")
        assertThat(input).contains("ResultGreen")
        assertThat(input).contains("Alignment.CenterHorizontally")
        assertThat(input).contains("TextAlign.Center")
        assertThat(input).contains("MaterialTheme.typography.displayLarge")
        assertThat(input).contains("\"\${formatValue(preview.totalWeightKg)} kg\"")
        assertThat(input).doesNotContain("text = \"kg\"")
    }

    private fun readSource(relativePath: String): String {
        val cwd = Path.of("").toAbsolutePath()
        val directPath = cwd.resolve(relativePath)
        val repoPath = cwd.resolve("app").resolve(relativePath)
        val path = if (directPath.toFile().exists()) directPath else repoPath

        return path.toFile().readText()
    }
}
