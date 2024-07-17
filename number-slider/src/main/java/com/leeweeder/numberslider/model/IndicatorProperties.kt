package com.leeweeder.numberslider.model

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.leeweeder.numberslider.NumberSlider

/**
 * Holds properties for customizing the appearance of value indicators in [NumberSlider].
 *
 * @param defaultStyle [TextStyle] for non-selected value indicators.
 * @param selectedStyle [TextStyle] for the selected value indicator.
 * */
data class IndicatorProperties(
    val defaultStyle: TextStyle = TextStyle.Default,
    val selectedStyle: TextStyle = defaultStyle.copy(fontWeight = FontWeight.Bold)
)