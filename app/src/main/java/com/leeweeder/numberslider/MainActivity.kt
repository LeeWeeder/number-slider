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

package com.leeweeder.numberslider

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.leeweeder.numberslider.ui.theme.NumberSliderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NumberSliderTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val weight = remember {
                        mutableFloatStateOf(1f)
                    }
                    LaunchedEffect(Unit) {
                        weight.floatValue = 25f
                    }
                    NumberSlider(value = weight.floatValue, onValueChange = {
                        weight.floatValue = it
                    }, onTextFieldValueChange = {
                        Log.d("TextFieldValue", it)
                    })
                }
            }
        }
    }
}