package com.leeweeder.numberslider.model

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

data class IndicatorProperties(
    val defaultStyle: TextStyle = TextStyle.Default,
    val selectedStyle: TextStyle = defaultStyle.copy(fontWeight = FontWeight.Bold)
)