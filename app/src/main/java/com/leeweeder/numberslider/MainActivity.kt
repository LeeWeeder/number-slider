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
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NumberSliderTheme {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val weight = remember {
                        mutableFloatStateOf(80f)
                    }
                    LaunchedEffect(Unit) {
                        delay(2000)
                        weight.floatValue = 40.4f
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