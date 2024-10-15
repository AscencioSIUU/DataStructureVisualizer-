package com.example.datastructurevisualizerapp.views


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datastructurevisualizerapp.data.data_source.NormalizedBar
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
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
        /*
        Spacer(
            modifier = Modifier
                .padding(bottom = labelHeight)
                .background(Purple40)
                .fillMaxSize()
                .drawWithCache {
                    onDrawBehind {
                        drawRect(
                            color = Color.Black,
                            style = Stroke(borderWidth.toPx())
                        )
                    }
                }
        )*/
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
fun TestingBarGraph(modifier: Modifier = Modifier, barGraphViewModel: BarGraphViewModel) {

    val coroutineScope = rememberCoroutineScope()
    var bubble by remember { mutableStateOf(false) }
    var selection by remember { mutableStateOf(false) }
    var quicksort by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize()){
        BarGraph(
            modifier = Modifier.padding(30.dp),
            normalizedBars = barGraphViewModel.normalizedBars
        )
        Button(
            onClick = {
                bubble = true
            }
        ) {
            Text(
                text= "BubbleSort",
                fontSize = 50.sp
            )
        }
        Button(
            onClick = {
                selection = true
            }
        ) {
            Text(
                text= "SelectionSort",
                fontSize = 50.sp
            )
        }
        Button(
            onClick = {
                quicksort = true
            }
        ) {
            Text(
                text= "InsertionSort",
                fontSize = 50.sp
            )
        }



    }

}

@Preview
@Composable
private fun Preview() {
    //val barGraphViewModel : BarGraphViewModel = BarGraphViewModel()
    //TestingBarGraph(barGraphViewModel = barGraphViewModel)
}






