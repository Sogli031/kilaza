package com.example.racunanjekilaze

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class CalculatorScreenUiTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun primaryScreenUsesSimplifiedCalculatorCopy() {
        composeRule.onNodeWithText("Računanje kilaže").assertIsDisplayed()
        composeRule.onNodeWithText("400 mm").assertIsDisplayed()
        composeRule.onNodeWithText("500 mm").assertIsDisplayed()
        composeRule.onNodeWithText("Proizvoljno").assertIsDisplayed()
    }

    @Test
    fun savedStripsTabIsShown() {
        composeRule.onNodeWithText("KALKULATOR").assertIsDisplayed()
        composeRule.onNodeWithText("TRAKE").assertIsDisplayed()
    }
}
