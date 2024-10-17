package com.example.datastructurevisualizerapp.algorithmLogic

import com.example.datastructurevisualizerapp.data.NormalizedBar
import kotlinx.coroutines.delay
import kotlin.math.min

val delayTime = 15L

class BubbleSortStrategy(): SortingStrategy {
    //var swapped: Boolean
    override suspend fun sort(normalizedList: MutableList<NormalizedBar>) {
        for (i in 0 until normalizedList.size-1){
            for(j in 0 until normalizedList.size - i - 1){
                if (normalizedList[j].normalizedHeight > normalizedList[j + 1].normalizedHeight) {
                    // Swap the bars

                    normalizedList[j].selected = true
                    delay(delayTime)

                    normalizedList[j + 1].selected = true
                    delay(delayTime)

                    val temp = normalizedList[j]

                    delay(delayTime)

                    normalizedList[j] = normalizedList[j + 1]
                    normalizedList[j +1] = temp

                    delay(delayTime)

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
                delay(delayTime)

                normalizedList[i].selected = true;
                delay(delayTime)

                //Swap bars
                val temp = normalizedList[i]
                normalizedList[i] = normalizedList[minIndex]
                normalizedList[minIndex] = temp
                delay(delayTime)

                normalizedList[minIndex].selected = false;
                normalizedList[i].selected = false;
            }
        }
    }
}

class MergeSortBottomUpStrategy: SortingStrategy {
    private lateinit var aux: MutableList<NormalizedBar>
    override suspend  fun sort(normalizedList: MutableList<NormalizedBar>) {
        aux = normalizedList.toMutableList()
        mergeSort(normalizedList, 0, normalizedList.size - 1)
    }

    private suspend fun mergeSort(normalizedList: MutableList<NormalizedBar>, left: Int, right: Int) {
        var mergingSize = 1
        while (mergingSize < normalizedList.size) {
            var currentLeft = 0
            while (currentLeft < normalizedList.size - mergingSize) {
                val currentRight = min(
                    currentLeft + 2 * mergingSize - 1,
                    normalizedList.size - 1
                )
                val currentMid = currentLeft + mergingSize - 1
                // O(2*mergingSize)
                merge(normalizedList, currentLeft, currentMid, currentRight)
                currentLeft += 2 * mergingSize
            }
            mergingSize *= 2
        }
    }

    private suspend fun merge(normalizedList: MutableList<NormalizedBar>, left: Int, mid: Int, right: Int) {

        for(i in left..right){
            aux[i] = normalizedList[i]
        }

        var currentBar: NormalizedBar
        var switchBar: NormalizedBar

        var leftIndex = left
        var rightIndex = mid + 1

        var index = left

        while (leftIndex <= mid && rightIndex <= right) {
            if(aux[leftIndex].normalizedHeight <= aux[rightIndex].normalizedHeight){
                normalizedList[index++] = aux[leftIndex++]

                if (index < normalizedList.size && leftIndex < aux.size) {
                    normalizedList[index].selected = true
                    aux[leftIndex].selected = true

                    delay(50L)

                    normalizedList[index].selected = false
                    aux[leftIndex].selected = false
                }
            }else{
                normalizedList[index++] = aux[rightIndex++]

                if (index < normalizedList.size && rightIndex < aux.size) {
                    normalizedList[index].selected = true
                    aux[rightIndex].selected = true

                    delay(50L)

                    normalizedList[index].selected = false
                    aux[rightIndex].selected = false
                }
            }

        }
        while (leftIndex <= mid) {
            normalizedList[index++] = aux[leftIndex++]

            if (index < normalizedList.size && leftIndex < aux.size) {
                normalizedList[index].selected = true
                aux[leftIndex].selected = true

                delay(50L)

                normalizedList[index].selected = false
                aux[leftIndex].selected = false
            }
        }
    }

}

// Kotlin program for implementation of QuickSort

class QuickSortStragegy: SortingStrategy {
    /* This function takes last element as pivot,
    places the pivot element at its correct
    position in sorted array, and places all
    smaller (smaller than pivot) to left of
    pivot and all greater elements to right
    of pivot */
    override suspend fun sort(normalizedList: MutableList<NormalizedBar>) {
        quickSortIterative(normalizedList, 0, normalizedList.size - 1)
    }

    private suspend fun partition(normalizedList: MutableList<NormalizedBar>, low: Int, high: Int): Int {
        val pivot = normalizedList[high].normalizedHeight

        // index of smaller element
        var i = low - 1
        for (j in low until high) {
            // If current element is smaller than or
            // equal to pivot
            if (normalizedList[j].normalizedHeight <= pivot) {
                i++

                normalizedList[i].selected = true
                normalizedList[j].selected = true
                delay(25L)

                // swap arr[i] and arr[j]
                val temp = normalizedList[i]
                normalizedList[i] = normalizedList[j]
                normalizedList[j] = temp
                delay(25L)

                normalizedList[i].selected = false
                normalizedList[j].selected = false
            }
        }

        normalizedList[i + 1].selected = true
        normalizedList[high].selected = true
        delay(25L)

        // swap arr[i+1] and arr[high] (or pivot)
        val temp = normalizedList[i + 1]
        normalizedList[i + 1] = normalizedList[high]
        normalizedList[high] = temp
        delay(25L)

        normalizedList[i + 1].selected = false
        normalizedList[high].selected = false

        return i + 1
    }

    /* A[] --> Array to be sorted,
   l  --> Starting index,
   h  --> Ending index */
    private suspend fun quickSortIterative(normalizedList: MutableList<NormalizedBar>, l: Int, h: Int) {
        // Create an auxiliary stack
        val stack = IntArray(h - l + 1)

        // initialize top of stack
        var top = -1

        // push initial values of l and h to stack
        stack[++top] = l
        stack[++top] = h

        // Keep popping from stack while is not empty
        while (top >= 0) {
            // Pop h and l
            var high = stack[top--]
            var low = stack[top--]

            // Set pivot element at its correct position
            // in sorted array
            val p = partition(normalizedList, low, high)

            // If there are elements on left side of pivot,
            // then push left side to stack
            if (p - 1 > low) {
                stack[++top] = low
                stack[++top] = p - 1
            }

            // If there are elements on right side of pivot,
            // then push right side to stack
            if (p + 1 < high) {
                stack[++top] = p + 1
                stack[++top] = high
            }
        }
    }
}
