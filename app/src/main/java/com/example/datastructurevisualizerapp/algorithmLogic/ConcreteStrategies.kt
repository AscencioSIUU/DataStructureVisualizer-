package com.example.datastructurevisualizerapp.algorithmLogic

import com.example.datastructurevisualizerapp.data.data_source.NormalizedBar
import kotlinx.coroutines.delay

class BubbleSortStrategy(): SortingStrategy {
    //var swapped: Boolean
    override suspend fun sort(normalizedList: MutableList<NormalizedBar>) {
        for (i in 0 until normalizedList.size-1){
            for(j in 0 until normalizedList.size - i - 1){
                if (normalizedList[j].normalizedHeight > normalizedList[j + 1].normalizedHeight) {
                    // Swap the bars

                    normalizedList[j].selected = true
                    delay(15L)

                    normalizedList[j + 1].selected = true
                    delay(15L)

                    val temp = normalizedList[j]

                    delay(15L)

                    normalizedList[j] = normalizedList[j + 1]
                    normalizedList[j +1] = temp

                    delay(15L)

                    normalizedList[j].selected = false
                    normalizedList[j + 1].selected = false
                    // Introduce a delay to animate the swap
                    // Delay for animation

                }
            }
        }
    }


}

class InsertionSortStrategy(): SortingStrategy {
    // Traverse through 1 to n

    override suspend fun sort(normalizedList: MutableList<NormalizedBar>) {
        for (i in 1 until normalizedList.size ) {


            val key = normalizedList[i] // The element to be inserted
            var j = i - 1


            key.selected = true;
            delay(15L)

            // Move elements of array[0..i-1] that are greater than key
            // to one position ahead of their current position

            normalizedList[j].selected = true
            delay(15L)

            while (j >= 0 && normalizedList[j].normalizedHeight > key.normalizedHeight && j <= normalizedList.size) {

                normalizedList[j].selected = true
                delay(15L)

                normalizedList[j + 1] = normalizedList[j]

                delay(15L)

                normalizedList[j].selected = false
                delay(15L)
                j -= 1
            }
            if(j >= 0){
                normalizedList[j].selected = false
            }


            // Place the key in its correct position
            normalizedList[j + 1] = key
            delay(15L)

            key.selected = false;
        }
    }

}

class SelectionSortStrategy(): SortingStrategy {

    override suspend fun sort(normalizedList: MutableList<NormalizedBar>) {
        for (i in 0 until normalizedList.size - 1) {
            // Find the minimum element in unsorted array
            var minIndex = i
            for (j in i + 1 until normalizedList.size) {
                if (normalizedList[j].normalizedHeight < normalizedList[minIndex].normalizedHeight) {
                    minIndex = j
                }
            }

            // Swap the found minimum element with the first element of the unsorted part
            if (minIndex != i) {
                //Select both bars
                normalizedList[minIndex].selected = true;
                delay(400L)

                normalizedList[i].selected = true;
                delay(400L)

                //Swap bars
                val temp = normalizedList[i]
                normalizedList[i] = normalizedList[minIndex]
                normalizedList[minIndex] = temp
                delay(400L)

                normalizedList[minIndex].selected = false;
                normalizedList[i].selected = false;
            }
        }
    }
}