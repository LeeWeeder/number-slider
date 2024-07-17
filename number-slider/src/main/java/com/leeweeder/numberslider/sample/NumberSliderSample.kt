package com.leeweeder.numberslider.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import com.leeweeder.numberslider.NumberSlider

@Composable
internal fun NumberSliderSample() {
    val value = remember {
        mutableFloatStateOf(20f)
    }
    NumberSlider(value = value.floatValue, onValueChange = {
        value.floatValue = it
    })
}