package com.example.datastructurevisualizerapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.net.Uri
import android.Manifest



class MainActivity : ComponentActivity() {

    // Crear una referencia al ViewModel
    private lateinit var dbViewModel: DbViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializar el ViewModel
        dbViewModel = androidx.lifecycle.ViewModelProvider(this, ViewModelProvider.Factory)[DbViewModel::class.java]

        setContent {
            val isConnected = checkForInternet(this)
            DataStructureVisualizerAppTheme {
                MyDataStructureVisualizerApp(isConnected)
            }
        }
    }

    // Manejar la respuesta después de seleccionar un archivo CSV
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_CSV_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                // Obtener el nombre del archivo y leer el contenido
                val fileName = getFileName(uri)
                Log.d("CSV File", "Selected CSV File: $fileName")
                readCsvFile(uri)
            }
        }
    }

    private val PICK_CSV_FILE_REQUEST_CODE = 1

    // Abrir explorador de archivos para seleccionar un archivo CSV
    fun startCsvFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
        }
        startActivityForResult(intent, PICK_CSV_FILE_REQUEST_CODE)
    }

    // Obtener el nombre del archivo CSV seleccionado
    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String {
        var name = ""
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    // Leer el archivo CSV y mostrar las primeras 3 líneas
    private fun readCsvFile(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val bufferedReader = inputStream?.bufferedReader()
            val csvContent = bufferedReader?.lineSequence()?.toList() ?: listOf()

            // Imprimir los primeros 3 números del CSV para verificar el contenido
            if (csvContent.isNotEmpty()) {
                val numbers = csvContent.take(3) // Tomar las primeras 3 líneas
                numbers.forEach { line ->
                    Log.d("CSV Content", "Línea: $line")
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Pedir permisos para acceder al almacenamiento
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
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
    var priceCoins by remember { mutableStateOf<List<Double>>(emptyList()) }
    val coinNames = listOf("bitcoin", "ethereum", "maker", "bittensor", "monero", "aave", "quant", "okb", "solana", "litecoin", "arweave" , "polkadot", "chainlink", "neo", "aptos", "uniswap", "helium", "celestia", "thorchain", "cosmos", "pendle", "filecoin", "sui", "apecoin")

    runBlocking {
        //if(isConected){
            withContext(Dispatchers.IO) {
                try {
                    val fetchedCoins = coinRepository.getAllCoins()
                    val fetchedCoinPrices = coinRepository.getCoinPrices(fetchedCoins)
                    coins = fetchedCoinPrices
                    //coins.forEach{coin ->
                    //    Log.d("CoinData", "Coin: ${coin.name}, Id: ${coin.id}, Symbol: ${coin.symbol} Price: ${coin.price}")
                    //}
                    //Log.d("ALL?", "${coins}")
                    Log.d("SEPARTOOOOR", "------------------------------------------------------------------------------")

                    val fetchedManualCoinPrices = coinRepository.getManualCoinPrices(coinNames)
                    fetchedManualCoinPrices.forEach{coinName ->
                        Log.d("CoinData", "Coin: ${coinName}")
                    }
                    dbViewModel.clearDb()
                    dbViewModel.insertAllCoins(fetchedCoinPrices)

                } catch (e: Exception) {
                    Log.e("CoinDataError", "${e.message}")
                }
            }
        //}
    }


    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val fetchedCoins = coinRepository.getAllCoins()
                val fetchedCoinPrices = coinRepository.getCoinPrices(fetchedCoins)
                coins = fetchedCoinPrices
                val pricesList = fetchedCoinPrices.mapNotNull { it.price }
                priceCoins = pricesList
                //priceCoins.forEach { price ->
                //    Log.d("prices", "$price")
                //}
                //coins.forEach{coin ->
                //    Log.d("CoinData", "Coin: ${coin.name}, Id: ${coin.id}, Symbol: ${coin.symbol} Price: ${coin.price}")
                //}
                val fetchedManualCoinPrices = coinRepository.getManualCoinPrices(coinNames)
                fetchedManualCoinPrices.forEach{coinName ->
                    Log.d("CoinData", "Coin: ${coinName}")
                }

            } catch (e: Exception) {
                Log.e("CoinDataError", "${e.message}")
            }
        }
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
            NavHost( navController = navController, startDestination = "createAccount") {


                var valuesList = priceCoins


                composable("login") {
                    LoginScreen(
                        viewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
                        navController = navController
                    ) { name, email, password ->
                        userName = name
                        userEmail = email
                        userPassword = password
                        isLoggedIn = true
                    }
                }

                composable("createAccount"){
                    CreateAccountScreen(navController)
                }

                composable("home") {
                    homeScreen(navController = navController)
                }

                composable("writeData") {
                    val context = LocalContext.current
                    val activity = context as? MainActivity

                    WriteData(
                        navController = navController,
                        dbViewModel = dbViewModel,  // Pasa el ViewModel como parámetro
                        onCsvSelectClick = { activity?.startCsvFilePicker() } // Usa LocalContext para llamar a la función de la actividad
                    )
                }


                composable("profile") {
                    userProfileScreen(
                        navController = navController,
                        onLogout = {
                            isLoggedIn = false  // Actualizar estado de login cuando se cierra sesión
                        }
                    )
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
                    BinaryTreesVisualizer(
                        valuesList = priceCoins,
                        onUseManualEntries = { dbViewModel.storedNumbers.value.map { it.toDouble() } }
                    )
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

