package com.example.datastructurevisualizerapp.screens


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize.Fill.calculateMainAxisPageSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round
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

        if(bubble){
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    barGraphViewModel.animatedBubbleSort()
                }
            }
        }
        if(selection){
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    barGraphViewModel.animatedSelectionSort()
                }
            }
        }
        if(quicksort){
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    barGraphViewModel.animatedInsertionSort()
                }
            }
        }


    }

}

@Preview
@Composable
private fun Preview() {
    val barGraphViewModel : BarGraphViewModel = BarGraphViewModel()
    TestingBarGraph(barGraphViewModel = barGraphViewModel)
}

data class BarModel(
    val height: Float,
    val label: String
)

class NormalizedBar(
    normalizedHeight: Float, label: String, initialIsSelected: Boolean
){
    val normalizedHeight: Float = normalizedHeight
    val label: String = label
    var selected by mutableStateOf(initialIsSelected)
}

fun generateBarModels(size: Int): List<BarModel> {
    val barModels = mutableListOf<BarModel>()
    val alphabet = ('A'..'Z').toList()

    for (i in 0 until size) {
        val height = Random.nextInt(1, 1001).toFloat()
        val label = alphabet[i % 26].toString() // Wraps around if size > 26
        barModels.add(BarModel(height, label))
    }

    return barModels
}

fun generateNormalizedBarModels(size: Int): List<NormalizedBar> {
    val barModels = mutableListOf<NormalizedBar>()
    val alphabet = ('A'..'Z').toList()

    for (i in 0 until size) {
        val height = Random.nextInt(1, 100).toFloat()/100
        val label = alphabet[i % 26].toString() // Wraps around if size > 26
        barModels.add(NormalizedBar(height, label, false))
    }

    return barModels
}

class BarGraphViewModel(barModels: List<BarModel> = generateBarModels(20), nomarlizedBars: List<NormalizedBar> = generateNormalizedBarModels(12)) {
    private val _barModels: List<BarModel> = barModels
    private var _normalizedBars = getBarModelLists().toMutableStateList()
    val normalizedBars: List<NormalizedBar>
        get() = _normalizedBars

    fun setListItem(index: Int, item: NormalizedBar){
        _normalizedBars[index] = item
    }

    suspend fun animatedBubbleSort() {
        //var swapped: Boolean

        for (i in 0 until _normalizedBars.size-1){
            for(j in 0 until _normalizedBars.size - i - 1){
                if (_normalizedBars[j].normalizedHeight > _normalizedBars[j + 1].normalizedHeight) {
                    // Swap the bars

                    _normalizedBars[j].selected = true
                    delay(15L)

                    _normalizedBars[j + 1].selected = true
                    delay(15L)

                    val temp = _normalizedBars[j]

                    delay(15L)

                    _normalizedBars[j] = _normalizedBars[j + 1]
                    _normalizedBars[j +1] = temp

                    delay(15L)

                    _normalizedBars[j].selected = false
                    _normalizedBars[j + 1].selected = false
                    // Introduce a delay to animate the swap
                    // Delay for animation

                }
            }
        }

    }

    suspend fun animatedSelectionSort() {

        // Traverse through all array elements
        for (i in 0 until _normalizedBars.size - 1) {
            // Find the minimum element in unsorted array
            var minIndex = i
            for (j in i + 1 until _normalizedBars.size) {
                if (_normalizedBars[j].normalizedHeight < _normalizedBars[minIndex].normalizedHeight) {
                    minIndex = j
                }
            }

            // Swap the found minimum element with the first element of the unsorted part
            if (minIndex != i) {
                //Select both bars
                _normalizedBars[minIndex].selected = true;
                delay(400L)

                _normalizedBars[i].selected = true;
                delay(400L)

                //Swap bars
                val temp = _normalizedBars[i]
                _normalizedBars[i] = _normalizedBars[minIndex]
                _normalizedBars[minIndex] = temp
                delay(400L)

                _normalizedBars[minIndex].selected = false;
                _normalizedBars[i].selected = false;

            }
        }
    }

    suspend fun animatedInsertionSort() {
        // Traverse through 1 to n
        for (i in 1 until _normalizedBars.size ) {


            val key = _normalizedBars[i] // The element to be inserted
            var j = i - 1


            key.selected = true;
            delay(15L)

            // Move elements of array[0..i-1] that are greater than key
            // to one position ahead of their current position

            _normalizedBars[j].selected = true
            delay(15L)

            while (j >= 0 && _normalizedBars[j].normalizedHeight > key.normalizedHeight && j <= _normalizedBars.size) {

                _normalizedBars[j].selected = true
                delay(15L)

                _normalizedBars[j + 1] = _normalizedBars[j]

                delay(15L)

                _normalizedBars[j].selected = false
                delay(15L)
                j -= 1
            }
            if(j >= 0){
                _normalizedBars[j].selected = false
            }


            // Place the key in its correct position
            _normalizedBars[j + 1] = key
            delay(15L)

            key.selected = false;
        }
    }

    private fun getBarModelLists(): List<NormalizedBar>{
        val maxHeight = getMaxHeight()
        val bars: MutableList<NormalizedBar> = mutableListOf()


        for(barModel in _barModels){

            bars.add(
                NormalizedBar(
                    normalizedHeight = barModel.height / maxHeight,
                    label = barModel.label,
                    initialIsSelected = false
                )
            )
        }
        return bars
    }

    private fun getMaxHeight(): Float{
        val heightList = getHeightList()
        return heightList.maxOrNull() ?: 0f
    }

    private fun getHeightList(): List<Float>
    {
        val list: MutableList<Float> = mutableListOf();
        for(barModel in _barModels){
            list.add(barModel.height)
        }
        return list.toList()
    }
}

