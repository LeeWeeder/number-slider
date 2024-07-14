package com.leeweeder.numberslider.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp

@Composable
internal fun calculateTextWidth(text: String, textStyle: TextStyle): Dp {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(
        text = AnnotatedString(text),
        style = textStyle
    )
    return with(density) {
        textLayoutResult.size.width.toDp()
    }
}