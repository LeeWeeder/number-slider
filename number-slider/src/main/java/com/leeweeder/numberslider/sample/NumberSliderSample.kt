/*
 * Number Slider Jetpack Compose. A customizable number slider component for Jetpack Compose, allowing users to select numeric values within a defined range.
 * Copyright (C) 2024 LeeWeeder
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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