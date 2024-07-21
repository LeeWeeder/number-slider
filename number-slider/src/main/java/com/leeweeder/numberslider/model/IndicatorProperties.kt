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