package com.example.datastructurevisualizerapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datastructurevisualizerapp.screens.CreateAccountScreen
import com.example.datastructurevisualizerapp.screens.LoginScreen
import com.example.datastructurevisualizerapp.screens.NavigationBar
import com.example.datastructurevisualizerapp.views.StackViewModel
import com.example.datastructurevisualizerapp.views.StackVisualizer
import com.example.datastructurevisualizerapp.screens.WriteData
import com.example.datastructurevisualizerapp.screens.topBar
import com.example.datastructurevisualizerapp.screens.homeScreen
import com.example.datastructurevisualizerapp.screens.userProfileScreen
import com.example.datastructurevisualizerapp.ui.theme.DataStructureVisualizerAppTheme
import com.example.datastructurevisualizerapp.views.BinaryTreesVisualizer
import com.example.datastructurevisualizerapp.views.BubbleSortVisualizer
import com.example.datastructurevisualizerapp.views.DoubleLinkedListsVisualizer
import com.example.datastructurevisualizerapp.views.HeapsVisualizer
import com.example.datastructurevisualizerapp.views.MergeSortVisualizer
import com.example.datastructurevisualizerapp.views.QueuesVisualizer
import com.example.datastructurevisualizerapp.views.QuickSortVisualizer
import com.example.datastructurevisualizerapp.views.SelectionSortVisualizer
import com.example.datastructurevisualizerapp.data.CoinRepository
import com.example.datastructurevisualizerapp.data.data_source.getBarData
import com.example.datastructurevisualizerapp.domain.models.Coin
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel
import com.example.datastructurevisualizerapp.viewmodels.DbViewModel
import com.example.datastructurevisualizerapp.views.InsertionSortVisualizer
import com.example.datastructurevisualizerapp.views.QueueViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isConnected = checkForInternet(this)
            DataStructureVisualizerAppTheme {
                MyDataStructureVisualizerApp(isConnected)
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}

@Composable
fun MyDataStructureVisualizerApp(isConected: Boolean) {

    val dbViewModel: DbViewModel = viewModel(factory = ViewModelProvider.Factory)

    var isLoggedIn by rememberSaveable  { mutableStateOf(false) }
    val navController = rememberNavController() //controlador de la navegación
    var userName by rememberSaveable { mutableStateOf("") }
    var userEmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }
    val coinRepository = CoinRepository()





    var coins by remember { mutableStateOf<List<Coin>>(emptyList()) }

    runBlocking {
        //if(isConected){
            withContext(Dispatchers.IO) {
                try {
                    val fetchedCoins = coinRepository.getAllCoins()
                    val fetchedCoinPrices = coinRepository.getCoinPrices(fetchedCoins)
                    coins = fetchedCoinPrices
                    coins.forEach{coin ->
                        Log.d("CoinData", "Coin: ${coin.name}, Id: ${coin.id}, Symbol: ${coin.symbol} Price: ${coin.price}")
                    }

                    dbViewModel.clearDb()
                    dbViewModel.insertAllCoins(fetchedCoinPrices)

                } catch (e: Exception) {
                    Log.e("CoinDataError", "${e.message}")
                }
            }
        //}

    }


    Scaffold(
        topBar = {
            topBar()
        },

        bottomBar = {
            if (isLoggedIn){
                NavigationBar(
                    navController,
                    user = userName,
                    email = userEmail,
                    password = userPassword
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val barData = dbViewModel.getCoinDataBar(25)
            val barGraphViewModel : BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks = barData.scaleMarks)
            NavHost( navController = navController, startDestination = "login") {


                var valuesList = listOf(23, 12, 45, 56, 86, 34, 56, 45, 34, 23, 12, 45, 67, 78, 76, 54, 67, 80, 90, 102, 230)

                composable("login") {
                    LoginScreen(navController) { name, email, password ->
                        userName = name
                        userEmail = email
                        userPassword = password
                        isLoggedIn = true
                    }
                }
                composable("createAccount"){
                    CreateAccountScreen(navController)
                }

                composable("home/{user}/{email}/{password}") { backStackEntry ->
                    val user = backStackEntry.arguments?.getString("user")
                    val email = backStackEntry.arguments?.getString("email")
                    val password = backStackEntry.arguments?.getString("password")

                    homeScreen(navController,user = user.orEmpty(), email = email.orEmpty(), password = password.orEmpty())
                }

                composable("writeData") {
                    WriteData(navController)
                }
                composable("profile/{user}/{email}/{password}") { backStackEntry ->

                    val user = backStackEntry.arguments?.getString("user")
                    val email = backStackEntry.arguments?.getString("email")
                    val password = backStackEntry.arguments?.getString("password")


                    userProfileScreen(user.toString(),email.toString(),password.toString(),navController)
                }

                //navegacion de todas las pantallas
                composable("Merge Sort") {
                    MergeSortVisualizer(barGraphViewModel = barGraphViewModel)
                }

                composable("Bubble Sort") {
                    BubbleSortVisualizer(barGraphViewModel = barGraphViewModel)
                }

                composable("Selection Sort") {
                    SelectionSortVisualizer(barGraphViewModel = barGraphViewModel)
                }


                composable("Insertion Sort") {
                    //val barGraphViewModel = BarGraphViewModel()
                    //TestingBarGraph(barGraphViewModel = barGraphViewModel)
                    InsertionSortVisualizer(barGraphViewModel = barGraphViewModel)
                }

                composable("Quick Sort") {
                    QuickSortVisualizer(barGraphViewModel = barGraphViewModel)
                }

                composable("Stacks") {

                    // Inicializar el ViewModel pasando valuesList
                    val viewModelStack = remember { StackViewModel(valuesList) }

                    // Llamar al visualizador del stack
                    StackVisualizer(viewModelStack)

                }

                composable("Queues") {

                    val viewModelQueue = remember { QueueViewModel(valuesList) }

                    QueuesVisualizer(viewModelQueue)
                }

                composable("Binary Trees") {
                    BinaryTreesVisualizer(valuesList)  // Llama a la función que maneja el árbol binario
                }


                composable("Heaps") {
                    HeapsVisualizer()
                }

                composable("Double Linked Lists") {
                    DoubleLinkedListsVisualizer(valuesList)
                }
            }
        }
    }


}

