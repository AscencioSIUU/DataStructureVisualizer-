package com.example.datastructurevisualizerapp.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datastructurevisualizerapp.algorithmLogic.SortingEnum
import com.example.datastructurevisualizerapp.data.NormalizedBar
import com.example.datastructurevisualizerapp.data.data_source.getBarData
import com.example.datastructurevisualizerapp.ui.theme.customColor1ContainerDark
import com.example.datastructurevisualizerapp.ui.theme.onCustomColor2Dark
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel


@Composable
fun AlgorithmScreen(modifier: Modifier = Modifier, barGraphViewModel: BarGraphViewModel, sortingEnum: SortingEnum) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        barGraphViewModel.setSortingStrategy(sortingEnum)
        BarGraph2(normalizedBars = barGraphViewModel.normalizedBars, scaleMarks = barGraphViewModel.scaleMarks)
        Button(
            onClick = { barGraphViewModel.sort() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
        ) {
            Text("Sort")
        }
        Button(
            onClick = { barGraphViewModel.resetData() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text("Reset")
        }
    }
}

@Composable
fun BarGraph(
    normalizedBars: List<NormalizedBar>,
    graphWidth: Dp = 395.dp,
    graphHeight: Dp = 500.dp,
    borderWidth: Dp = 4.dp,
    upperGap: Dp = 25.dp,
    labelHeight: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(25.dp)
            .width(graphWidth)
            .height(graphHeight)
    ){

        Spacer(
            modifier = Modifier
                .padding(bottom = labelHeight)
                .fillMaxSize()
                .drawBehind {
                    drawRect(
                        color = Color.Black,
                        style = Stroke(borderWidth.toPx())
                    )
                }
        )

        Spacer(
            modifier = Modifier
                .drawWithCache {
                    val barWidth = (size.width) / normalizedBars.size
                    onDrawBehind {
                        repeat(normalizedBars.size) { i ->
                            val offset = Offset(
                                barWidth * i,
                                size.height
                            )
                            val height = -normalizedBars[i].normalizedHeight * size.height
                            drawRect(
                                color = if (normalizedBars[i].selected) Color.Yellow else Color.Blue,
                                topLeft = offset,
                                size = Size(
                                    width = barWidth,
                                    height = if (height < 0) height else -size.height
                                )
                            )

                        }
                    }
                }
        )

        Spacer(
            modifier = Modifier
                .padding(
                    start = borderWidth / 2,
                    end = borderWidth / 2,
                    top = borderWidth / 2 + upperGap,
                    bottom = borderWidth / 2 + labelHeight
                )
                .fillMaxSize()
                .drawWithCache {
                    val barWidth = (size.width) / normalizedBars.size

                    onDrawBehind {

                        repeat(normalizedBars.size) { i ->
                            val offset = Offset(
                                barWidth * i,
                                size.height
                            )
                            val height = -normalizedBars[i].normalizedHeight * size.height
                            drawRect(
                                color = if (normalizedBars[i].selected) Color.Yellow else Color.Blue,
                                topLeft = offset,
                                size = Size(
                                    width = barWidth,
                                    height = if (height < 0) height else -size.height
                                )
                            )

                        }

                    }
                }

        )
    }
}

@Composable
fun BarGraph2(
    normalizedBars: List<NormalizedBar>,
    scaleMarks: List<Float>,
    graphWidth: Dp = 395.dp,
    graphHeight: Dp = 500.dp,
    borderWidth: Dp = 7.dp,
    upperGap: Dp = 25.dp,
    verticalLabelWidth: Dp = 50.dp,
    horizontalLabelHeight: Dp = 50.dp,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val defultColor = onCustomColor2Dark
    val selectedColor = customColor1ContainerDark
    val textColor = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = Modifier
            .padding(25.dp)
            .background(MaterialTheme.colorScheme.surface)
            .width(graphWidth)
            .height(graphHeight)
    ){
        Spacer(
            modifier = Modifier
                .padding(
                    bottom = horizontalLabelHeight,
                )
                .fillMaxSize()
                .drawBehind {

                    val offset = Offset(
                        x = verticalLabelWidth.toPx(),
                        y = 0f
                    )

                    val target = borderWidth.toPx() / 4 + upperGap.toPx()
                    val axe = size.height - target
                    val step = axe / (scaleMarks.size - 1)

                    /*
                    drawRect(
                        color = Color.Black,
                        style = Stroke(borderWidth.toPx()),
                        topLeft = offset
                    )*/


                    scaleMarks.forEachIndexed { index, fl ->
                        drawText(
                            brush = SolidColor(textColor),
                            textLayoutResult =  textMeasurer.measure(
                                text = fl
                                    .toString(),
                                style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center)
                            ),
                            topLeft = offset.copy(
                                x = verticalLabelWidth.toPx() - borderWidth.toPx() * 4,
                                y = (size.height - borderWidth.toPx() * 2) - index * step
                            )
                        )
                    }

                }
        )

        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val barWidth =
                        (size.width - borderWidth.toPx() - verticalLabelWidth.toPx()) / normalizedBars.size
                    val barMaxHeight =
                        (size.height - upperGap.toPx() - borderWidth.toPx() - horizontalLabelHeight.toPx())
                    val gradient = Brush.radialGradient(
                        listOf(selectedColor.copy(.9f), selectedColor, selectedColor.copy(.9f)),
                        center = Offset(300f,300f),
                        radius = 500f
                    )

                    onDrawBehind {

                        repeat(normalizedBars.size) { i ->
                            val offset = Offset(
                                (barWidth * i) + borderWidth.toPx() / 2 + verticalLabelWidth.toPx(),
                                size.height - (borderWidth.toPx() / 2 + horizontalLabelHeight.toPx())
                            )
                            drawRect(
                                gradient,
                                topLeft = offset,
                                size = Size(
                                    width = barWidth,
                                    height = -normalizedBars[i].normalizedHeight * barMaxHeight
                                ),
                                style = Stroke(width = 2.dp.toPx())
                            )
                            drawRect(
                                color = if (normalizedBars[i].selected) selectedColor else defultColor,
                                topLeft = offset,
                                size = Size(
                                    width = barWidth,
                                    height = -normalizedBars[i].normalizedHeight * barMaxHeight
                                )
                            )


                            /*
                            drawText(
                                brush = SolidColor(textColor),
                                textLayoutResult =  textMeasurer.measure(
                                    text = normalizedBars[i].label,
                                    constraints = Constraints.fixedWidth(barWidth.toInt()),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                    )
                                ),
                                topLeft = offset.copy(y = size.height - horizontalLabelHeight.toPx() + borderWidth.toPx())
                            )*/
                            drawRotatedText(
                                brush = SolidColor(textColor),
                                textLayoutResult =  textMeasurer.measure(
                                    text = normalizedBars[i].label,
                                    constraints = Constraints.fixedWidth(barWidth.toInt()*2),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Left,
                                    )
                                ),
                                topLeft = offset.copy(x = (barWidth * i) + borderWidth.toPx() * 2.5f + verticalLabelWidth.toPx(), y = size.height - horizontalLabelHeight.toPx() + borderWidth.toPx()),
                                angle = 90f
                            )

                        }

                    }
                }
        )
    }
}



@OptIn(ExperimentalTextApi::class)
fun DrawScope.drawRotatedText(
    brush: Brush,
    angle: Float,
    topLeft: Offset,
    textLayoutResult: TextLayoutResult
) {
    rotate(degrees = angle, pivot = topLeft) { // Rotate around the given offset
        drawText(
            brush = brush,
            textLayoutResult = textLayoutResult,
            topLeft = topLeft,
        )
    }
}


@Composable
fun TestingBarGraph(modifier: Modifier = Modifier, barGraphViewModel: BarGraphViewModel) {


    //Column(modifier = Modifier.fillMaxSize()){
        BarGraph2(
            modifier = Modifier.padding(30.dp),
            normalizedBars = barGraphViewModel.normalizedBars,
            scaleMarks = barGraphViewModel.scaleMarks
        )

    //}

}

@Preview
@Composable
private fun TestingNormalized() {
    val barData = getBarData()
    val barGraphViewModel : BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks =  barData.scaleMarks)
    Column(){
        barGraphViewModel.normalizedBars.forEachIndexed { i, bar ->
            Text(text = bar.normalizedHeight.toString())
        }
    }

}

@Preview
@Composable
private fun TestingScale() {
    val barData = getBarData()
    val barGraphViewModel : BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks =  barData.scaleMarks)
    Column(){
        barData.scaleMarks.forEachIndexed { i, bar ->
            Text(text = bar.toString())
        }
    }

}

@Preview
@Composable
private fun Preview() {
    val barData = getBarData()
    val barGraphViewModel : BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks =  barData.scaleMarks)
    TestingBarGraph(barGraphViewModel = barGraphViewModel)
}






