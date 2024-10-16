package com.example.datastructurevisualizerapp.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datastructurevisualizerapp.data.NormalizedBar
import com.example.datastructurevisualizerapp.data.data_source.getBarData
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val barGraphViewModel = BarGraphViewModel()
            TestingBarGraph(barGraphViewModel = barGraphViewModel)
        }
    }
}
*/
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

    Box(
        modifier = Modifier
            .background(Color.Magenta)
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
                        y= 0f
                    )

                    val target = borderWidth.toPx()/4 + upperGap.toPx()
                    val axe = size.height - target
                    val step = axe/(scaleMarks.size - 1)

                    drawRect(
                        color = Color.Black,
                        style = Stroke(borderWidth.toPx()),
                        topLeft = offset
                    )


                    scaleMarks.forEachIndexed { index, fl ->
                        drawText(
                            textMeasurer.measure(
                                text = fl.toString(),
                                style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center)
                            ),
                            topLeft = offset.copy(
                                x = verticalLabelWidth.toPx() - borderWidth.toPx() *4,
                                y = size.height - index * step
                            )
                        )
                    }

                }
        )

        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val barWidth = (size.width - borderWidth.toPx() - verticalLabelWidth.toPx()) / normalizedBars.size
                    val barMaxHeight = (size.height - upperGap.toPx() - borderWidth.toPx() - horizontalLabelHeight.toPx())

                    onDrawBehind {

                        repeat(normalizedBars.size) { i ->
                            val offset = Offset(
                                (barWidth * i) + borderWidth.toPx()/2 + verticalLabelWidth.toPx(),
                                size.height - (borderWidth.toPx()/2 + horizontalLabelHeight.toPx())
                            )
                            drawRect(
                                color = if (normalizedBars[i].selected) Color.Yellow else Color.Blue,
                                topLeft = offset,
                                size = Size(
                                    width = barWidth,
                                    height = -normalizedBars[i].normalizedHeight * barMaxHeight
                                )
                            )
                            drawText(
                                textMeasurer.measure(
                                    text = normalizedBars[i].label,
                                    constraints = Constraints.fixedWidth(barWidth.toInt()),
                                    style = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center)
                                ),
                                topLeft = offset.copy(y = size.height - horizontalLabelHeight.toPx() + borderWidth.toPx())
                            )

                        }

                    }
                }
        )
    }
}


@Composable
fun ChatPBarGraph(
    normalizedBars: List<NormalizedBar>,
    modifier: Modifier = Modifier,
    graphWidth: Dp = 395.dp,
    graphHeight: Dp = 500.dp,
    borderWidth: Dp = 4.dp,
    upperGap: Dp = 25.dp,
    labelHeight: Dp = 20.dp
) {
    val textMeasurer = rememberTextMeasurer() // Used to measure text size

    // Draw the entire graph and labels using drawWithCache for optimal performance
    Box(
        modifier = Modifier
            .background(Color.White)
            .width(graphWidth)
            .height(graphHeight)

            //.height(graphHeight + labelHeight)
    ){
        Spacer(modifier = Modifier

            .drawWithCache {
                val barWidth = size.width / normalizedBars.size

                onDrawBehind {
                    // Draw the border
                    drawRect(
                        color = Color.Black,
                        style = Stroke(width = borderWidth.toPx())
                    )

                    // Draw bars and labels for each normalized bar
                    normalizedBars.forEachIndexed { index, bar ->
                        val barHeight = bar.normalizedHeight * (size.height - labelHeight.toPx())
                        val offsetX = index * barWidth

                        // Draw each bar
                        drawRect(
                            color = if (bar.selected) Color.Yellow else Color.Blue,
                            topLeft = Offset(offsetX, size.height - barHeight - labelHeight.toPx()),
                            size = Size(barWidth, barHeight)
                        )

                        // Measure and draw the label text at the bottom
                        val labelResult = textMeasurer.measure(bar.label)
                        drawText(
                            textLayoutResult = labelResult,
                            topLeft = Offset(
                                x = offsetX + (barWidth - labelResult.size.width) / 2,
                                y = size.height - labelHeight.toPx() / 2
                            )
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun TestingBarGraph(modifier: Modifier = Modifier, barGraphViewModel: BarGraphViewModel) {


    //Column(modifier = Modifier.fillMaxSize()){
        BarGraph2(
            modifier = Modifier.padding(30.dp),
            normalizedBars = barGraphViewModel.normalizedBars,
            scaleMarks = barGraphViewModel.heights
        )

    //}

}

@Preview
@Composable
private fun TestingNormalized() {
    val barData = getBarData()
    val barGraphViewModel : BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, heights =  barData.scaleMarks)
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
    val barGraphViewModel : BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, heights =  barData.scaleMarks)
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
    val barGraphViewModel : BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, heights =  barData.scaleMarks)
    TestingBarGraph(barGraphViewModel = barGraphViewModel)
}






