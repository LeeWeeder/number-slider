package com.leeweeder.numberslider

import androidx.annotation.FloatRange
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.leeweeder.numberslider.model.IndicatorProperties
import com.leeweeder.numberslider.util.calculateTextWidth
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.round

private const val POINTS_ITEM_WIDTH = 3

/**
 * A number slider that allows users to select a numeric value within the given [minValue] - [maxValue] inclusive .
 *
 * @sample com.leeweeder.numberslider.sample.NumberSliderSample
 *
 * @param modifier the [Modifier] to be applied to [NumberSlider].
 * @param minValue the minimum selectable value.
 * @param maxValue the maximum selectable value.
 * @param step the increment between selectable values.
 * @param value the value of this [NumberSlider].
 * @param indicatorProperties [IndicatorProperties] for customizing the appearance of value indicators.
 * @param rulerMarker [Composable] for customizing markers for non-whole number values.
 * @param valueTextFieldStyle [TextStyle] for the text field displaying the selected value.
 * @param centerMarker [Composable] for customizing center marker of the slider.
 * @param onTextFieldValueChange callback invoked when the text field value changes.
 * @param onValueChange callback invoked when the selected value (of the slider itself) changes.
 * */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NumberSlider(
    modifier: Modifier = Modifier,
    minValue: Int = 1,
    maxValue: Int = 999,
    @FloatRange(from = 0.0, to = 1.0) step: Float = 0.1f,
    value: Float = minValue.toFloat(),
    indicatorProperties: IndicatorProperties = IndicatorProperties(defaultStyle = MaterialTheme.typography.displayMedium),
    rulerMarker: @Composable (BoxScope.() -> Unit)? = {
        Box(
            modifier = Modifier
                .width(1.5.dp)
                .height(16.dp)
                .background(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        )
    },
    valueTextFieldStyle: TextStyle = MaterialTheme.typography.displayMedium.copy(
        color = MaterialTheme.colorScheme.outline
    ),
    centerMarker: @Composable (BoxScope.() -> Unit) = {
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(100.dp)
                .background(color = MaterialTheme.colorScheme.outline)
        )
    },
    onTextFieldValueChange: ((String) -> Unit)? = null,
    onValueChange: (Float) -> Unit
) {
    require(value in minValue.toFloat()..maxValue.toFloat()) {
        "Value must be in the range $minValue to $maxValue"
    }
    val density = LocalDensity.current
    val wholeNumberItemWidth = max(
        calculateTextWidth(
            text = maxValue.toString(),
            textStyle = indicatorProperties.defaultStyle
        ).value, calculateTextWidth(
            text = maxValue.toString(),
            textStyle = indicatorProperties.selectedStyle
        ).value
    ).dp
    val numbers = List(((maxValue - minValue) / step + 1).toInt()) { index ->
        val currentValue = minValue + index * step
        round(currentValue * 10) / 10
    }

    fun getOffset(itemWidth: Int): Int {
        return with(density) {
            (itemWidth / 2) - (wholeNumberItemWidth.roundToPx() / 2)
        }
    }

    fun getOffset(itemWidth: Dp): Int {
        return with(density) {
            (itemWidth.roundToPx() / 2) - (wholeNumberItemWidth.roundToPx() / 2)
        }
    }

    fun getItemWidth(value: Float): Dp {
        return if (value % 1 == 0f) {
            wholeNumberItemWidth
        } else {
            POINTS_ITEM_WIDTH.dp
        }
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val initialItemWidth = getItemWidth(value)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = numbers.indexOf(value),
        initialFirstVisibleItemScrollOffset = getOffset(initialItemWidth)
    )

    val textFieldValue = remember {
        mutableStateOf(TextFieldValue(text = value.toString()))
    }

    LaunchedEffect(value) {
        textFieldValue.value = TextFieldValue(text = value.toString())
        val itemWidth = getItemWidth(value)
        val offset = getOffset(itemWidth)
        if (!listState.isScrollInProgress) {
            listState.scrollToItem(numbers.indexOf(value), offset)
        }
    }

    // Calculate the index of the item closest to the center
    val centerIndex = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val centerX = layoutInfo.centerX
            var closestItemIndex = 0
            var closestDistance = Float.MAX_VALUE
            for (item in visibleItems) {
                val itemCenterX = item.offset + (item.size / 2)
                val distance = abs(centerX - itemCenterX)
                if (distance < closestDistance) {
                    closestDistance = distance.toFloat()
                    closestItemIndex = item.index
                } else {
                    break
                }
            }
            closestItemIndex.coerceIn(0, numbers.size - 1)
        }
    }

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val scope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    LaunchedEffect(centerIndex.value) {
        val newCenterValue = numbers[centerIndex.value]
        if (newCenterValue != value) {
            focusManager.clearFocus()
            onValueChange(newCenterValue)
        }
    }

    val isKeyboardClosing = isKeyboardClosing()

    LaunchedEffect(key1 = isKeyboardClosing) {
        if (isKeyboardClosing) {
            focusManager.clearFocus()
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Box(contentAlignment = Alignment.TopCenter) {
            LazyRow(
                state = listState,
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = screenWidth / 2 - (wholeNumberItemWidth / 2)),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                flingBehavior = flingBehavior
            ) {
                itemsIndexed(numbers, key = { _, item ->
                    item
                }) { index, number ->
                    val itemWidth by remember {
                        mutableStateOf(if (number % 1 == 0f) wholeNumberItemWidth else POINTS_ITEM_WIDTH.dp)
                    }
                    NumberItem(
                        value = number,
                        isSelected = index == centerIndex.value,
                        indicatorProperties = indicatorProperties,
                        rulerMarker = rulerMarker,
                        modifier = Modifier
                            .width(itemWidth)
                    ) {
                        scope.launch {
                            val offset = getOffset(itemWidth)
                            listState.animateScrollToItem(index = index, scrollOffset = offset)
                        }
                    }
                }
            }
            centerMarker()
        }

        val keepWholeSelection = remember {
            mutableStateOf(false)
        }
        if (keepWholeSelection.value) {
            SideEffect {
                keepWholeSelection.value = false
            }
        }

        ValueTextField(
            value = textFieldValue.value,
            maxValue = maxValue,
            style = valueTextFieldStyle,
            onValueChange = { newValue ->
                val text = newValue.text
                if (text.isEmpty()) {
                    textFieldValue.value = newValue
                    return@ValueTextField
                }
                if (text.substringAfter('.', "").length > 1) return@ValueTextField
                val floatValue = text.toFloatOrNull() ?: return@ValueTextField
                if (floatValue < minValue || floatValue > maxValue) return@ValueTextField

                if (keepWholeSelection.value) {
                    keepWholeSelection.value = false
                    textFieldValue.value =
                        newValue.copy(selection = TextRange(0, newValue.text.length))
                } else {
                    textFieldValue.value = newValue
                }
                onTextFieldValueChange?.let {
                    it(textFieldValue.value.text)
                }
            },
            modifier = Modifier.onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    val text = textFieldValue.value.text
                    textFieldValue.value =
                        textFieldValue.value.copy(selection = TextRange(0, text.length))
                    keepWholeSelection.value = true
                }
            }) {
            scope.launch {
                val floatValue = textFieldValue.value.text.toFloatOrNull() ?: return@launch
                val itemIndex = numbers.indexOf(floatValue)
                if (itemIndex >= 0) {
                    listState.scrollToItem(index = itemIndex)

                    val visibleItemIndex =
                        listState.layoutInfo.visibleItemsInfo.indexOfFirst { itemInfo ->
                            (itemInfo.key as? Float)?.let {
                                it == numbers[itemIndex]
                            } ?: false
                        }
                    if (visibleItemIndex >= 0) {
                        val itemInfo =
                            listState.layoutInfo.visibleItemsInfo[visibleItemIndex]
                        itemInfo.let {
                            val offset = getOffset(it.size)
                            listState.animateScrollToItem(
                                index = itemIndex,
                                scrollOffset = offset
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NumberItem(
    value: Float,
    isSelected: Boolean,
    indicatorProperties: IndicatorProperties,
    modifier: Modifier = Modifier,
    rulerMarker: (@Composable BoxScope.() -> Unit)?,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(110.dp)
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        val style =
            if (isSelected) indicatorProperties.selectedStyle else indicatorProperties.defaultStyle
        if (value % 1 == 0f) {
            Text(
                text = value.toInt().toString(),
                style = style
            )
        }

        if (rulerMarker != null && value % 1 != 0f) {
            rulerMarker()
        }
    }
}

@Composable
private fun ValueTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    maxValue: Int,
    style: TextStyle,
    modifier: Modifier = Modifier,
    onDone: () -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = style.copy(
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier
            .width(
                calculateTextWidth(
                    text = "$maxValue.0",
                    textStyle = style
                ) + 8.dp
            ),
        keyboardActions = KeyboardActions(onDone = {
            onDone()
        })
    )
}

private val LazyListLayoutInfo.centerX: Int
    get() = this.viewportSize.center.x + this.viewportStartOffset

@Composable
private fun isKeyboardClosing(): Boolean {
    val isKeyBoardClosing = remember {
        mutableStateOf(false)
    }
    val imeInsets = WindowInsets.ime.asPaddingValues()
    val previousBottomPadding = remember {
        mutableStateOf(0.dp)
    }
    LaunchedEffect(key1 = imeInsets.calculateBottomPadding()) {
        isKeyBoardClosing.value = imeInsets.calculateBottomPadding() < previousBottomPadding.value
        previousBottomPadding.value = imeInsets.calculateBottomPadding()
    }
    return isKeyBoardClosing.value
}