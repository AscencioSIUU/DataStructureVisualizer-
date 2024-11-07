package com.example.datastructurevisualizerapp.components.visualizationDataComponents

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class SelectionMode {
    Single,
    Multiple
}

interface ChipSelectorState {
    val chips: List<String>
    val selectedChips: List<String>

    fun onChipClick(chip: String)
    fun isSelected(chip: String): Boolean
}

class ChipSelectorStateImpl(
    // 1
    override val chips: List<String>,
    // 2
    selectedChips: List<String> = emptyList(),
    // 3
    val mode: SelectionMode = SelectionMode.Single,
) : ChipSelectorState {
    // 4
    override var selectedChips by mutableStateOf(selectedChips)

    // 5
    override fun onChipClick(chip: String) {
        if (mode == SelectionMode.Single) {
            if (!selectedChips.contains(chip)) {
                selectedChips = listOf(chip)
            }
        } else {
            selectedChips = if (selectedChips.contains(chip)) {
                selectedChips - chip
            } else {
                selectedChips + chip
            }
        }
    }

    // 6
    override fun isSelected(chip: String): Boolean = selectedChips.contains(chip)

    companion object {
        // 7
        val saver = Saver<ChipSelectorStateImpl, List<*>>(
            save = { state ->
                buildList {
                    add(state.chips.size)
                    addAll(state.chips)
                    add(state.selectedChips.size)
                    addAll(state.selectedChips)
                    add(state.mode.ordinal)
                }
            },
            restore = { items ->
                var index = 0
                val chipsSize = items[index++] as Int
                val chips = List(chipsSize) {
                    items[index++] as String
                }
                val selectedSize = items[index++] as Int
                val selectedChips = List(selectedSize) {
                    items[index++] as String
                }
                val mode = SelectionMode.entries[items[index] as Int]
                ChipSelectorStateImpl(
                    chips = chips,
                    selectedChips = selectedChips,
                    mode = mode,
                )
            }
        )
    }
}

// 8
@Composable
fun rememberChipSelectorState(
    chips: List<String>,
    selectedChips: List<String> = emptyList(),
    mode: SelectionMode = SelectionMode.Single,
): ChipSelectorState {
    if (chips.isEmpty()) error("No chips provided")
    if (mode == SelectionMode.Single && selectedChips.size > 1) {
        error("Single choice can only have 1 pre-selected chip")
    }

    return rememberSaveable(
        saver = ChipSelectorStateImpl.saver
    ) {
        ChipSelectorStateImpl(
            chips,
            selectedChips,
            mode,
        )
    }
}

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val pathMeasure = remember { PathMeasure() }
    val path = remember { Path() }
    val pathSegment = remember { Path() }
    val interactionSource = remember { MutableInteractionSource() }
    val transition = updateTransition(targetState = isSelected, label = "transition")

    val pathFraction by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "pathSegment"
    ){
            selected ->
        if(selected) 1f else 0f
    }

    val backgroundColor by transition.animateColor(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "backgroundColor"
    ) { selected ->
        if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    }

    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer

    val borderColor = MaterialTheme.colorScheme.onSurface
    val borderWidth = with(LocalDensity.current) { 2.dp.toPx()}

    val textAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "textAlpha"
    ) { selected ->
        if (selected) 1f else .6f
    }



    Box(
        modifier = modifier
            .drawWithCache {
                val cornerRadius = size.height / 2f
                path.moveTo(borderWidth + size.width / 2f, borderWidth)
                path.lineTo(size.width - borderWidth - cornerRadius, borderWidth)
                path.quadraticTo(
                    size.width - borderWidth,
                    borderWidth,
                    size.width - borderWidth,
                    borderWidth + cornerRadius
                )
                path.quadraticTo(
                    size.width - borderWidth,
                    size.height - borderWidth,
                    size.width - borderWidth - cornerRadius,
                    size.height - borderWidth
                )
                path.lineTo(borderWidth + cornerRadius, size.height - borderWidth)
                path.quadraticTo(
                    borderWidth,
                    size.height - borderWidth,
                    borderWidth,
                    size.height - cornerRadius - borderWidth
                )
                path.quadraticTo(
                    borderWidth,
                    borderWidth,
                    borderWidth + cornerRadius,
                    borderWidth
                )
                path.close()
                // 4
                pathMeasure.setPath(path, false)
                // 5
                pathSegment.reset()
                // 6
                pathMeasure.getSegment(0f, pathMeasure.length * pathFraction, pathSegment)
                onDrawBehind {
                    drawPath(
                        path = path,
                        color = backgroundColor,
                        style = Fill,
                    )
                    // 7
                    drawPath(
                        path = pathSegment,
                        color = borderColor,
                        style = Stroke(width = borderWidth),
                    )
                }
            }
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
    ) {
        Text(
            text = label,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .graphicsLayer { alpha = textAlpha }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class)
@Composable
fun ChipsSelector(
    // 1
    state: ChipSelectorState,
    // 2
    modifier: Modifier = Modifier,
) {
    // 3
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 4
        state.chips.forEach { chip ->
            Chip(
                label = chip,
                isSelected = state.isSelected(chip),
                onClick = { state.onChipClick(chip) }
            )
        }
    }
}

@Preview
@Composable
private fun ChipPreview() {
    val chipState: ChipSelectorStateImpl = ChipSelectorStateImpl(listOf("Avocado", "Strawberry", "Lemon"))
    ChipsSelector(state = chipState)
}