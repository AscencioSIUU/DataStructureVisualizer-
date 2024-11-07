package com.example.datastructurevisualizerapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datastructurevisualizerapp.data.CoinRepository
import com.example.datastructurevisualizerapp.data.data_source.getBarData
import com.example.datastructurevisualizerapp.domain.models.Coin
import com.example.datastructurevisualizerapp.screens.CreateAccountScreen
import com.example.datastructurevisualizerapp.screens.LoginScreen
import com.example.datastructurevisualizerapp.screens.NavigationBar
import com.example.datastructurevisualizerapp.screens.VisualizationDataInputScreen
import com.example.datastructurevisualizerapp.screens.WriteData
import com.example.datastructurevisualizerapp.screens.topBar
import com.example.datastructurevisualizerapp.screens.homeScreen
import com.example.datastructurevisualizerapp.screens.userProfileScreen
import com.example.datastructurevisualizerapp.ui.theme.DataStructureVisualizerAppTheme
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel
import com.example.datastructurevisualizerapp.viewmodels.DbViewModel
import com.example.datastructurevisualizerapp.views.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayList



class MainActivity : ComponentActivity() {

    // Crear una referencia al ViewModel
    private lateinit var dbViewModel: DbViewModel

    // Manejador del Activity Result para la selección de archivos
    private val selectCsvLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val fileName = getFileName(it)

            if (fileName.endsWith(".csv", ignoreCase = true)) {
                Log.d("CSV File", "Selected CSV File: $fileName")
                readCsvFile(it)  // Solo leer si es un archivo CSV
            } else {
                Log.e("File Selection", "El archivo seleccionado no es un CSV.")
            }
        }
    }

    // Manejador de permisos usando la API moderna
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Permissions", "Permiso concedido")
        } else {
            Log.d("Permissions", "Permiso denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Llama a checkPermissions() para asegurarse de que los permisos estén concedidos
        checkPermissions()

        // Inicializar el ViewModel
        dbViewModel = androidx.lifecycle.ViewModelProvider(this, ViewModelProvider.Factory)[DbViewModel::class.java]

        setContent {
            val isConnected = checkForInternet(this)
            DataStructureVisualizerAppTheme {
                MyDataStructureVisualizerApp(isConnected)
            }
        }
    }

    // Abrir explorador de archivos para seleccionar un archivo
    fun startCsvFilePicker() {
        selectCsvLauncher.launch("*/*")
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

            // Convertir las líneas del archivo CSV en una lista de números
            val numbers = csvContent.flatMap { line ->
                line.split(",").mapNotNull { it.trim().toIntOrNull() }
            }

            // Actualizar los números guardados en el ViewModel
            dbViewModel.updateCsvData(numbers)

            // Log para verificar las primeras tres líneas
            if (csvContent.isNotEmpty()) {
                val preview = csvContent.take(3)
                preview.forEachIndexed { index, line ->
                    Log.d("CSV Content", "Línea $index: $line")
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    // Pedir permisos para acceder al almacenamiento
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    // El permiso ya ha sido concedido
                    Log.d("Permissions", "El permiso ya está concedido")
                }
                else -> {
                    // Pedir permiso al usuario
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}

@Composable
fun MyDataStructureVisualizerApp(isConnected: Boolean) {
    val dbViewModel: DbViewModel = viewModel(factory = ViewModelProvider.Factory)
    var isLoggedIn by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()
    var userName by rememberSaveable { mutableStateOf("") }
    var userEmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }
    val coinRepository = CoinRepository()

    var coins by remember { mutableStateOf<List<Coin>>(emptyList()) }
    var priceCoins by remember { mutableStateOf<List<Double>>(emptyList()) }
    val coinNames = listOf("bitcoin", "ethereum", "maker", "bittensor", "monero", "aave", "quant", "okb", "solana", "litecoin", "arweave", "polkadot", "chainlink", "neo", "aptos", "uniswap", "helium", "celestia", "thorchain", "cosmos", "pendle", "filecoin", "sui", "apecoin")
    var barData = dbViewModel.getCoinDataBar(25)
    var barGraphViewModel: BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks = barData.scaleMarks)


    var dataSourceType by remember {
        mutableStateOf("Random")
    }

    if(dataSourceType == "Random"){
        barData = getBarData()
        val doubles: MutableList<Double> = mutableListOf()
        for (item in barData.heights)  {
            doubles.add(item.toDouble())
        }
        priceCoins = doubles
        barGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks = barData.scaleMarks)
    }else if (dataSourceType == "Crypto"){
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                try {
                    val fetchedCoins = coinRepository.getAllCoins()
                    val fetchedCoinPrices = coinRepository.getCoinPrices(fetchedCoins)
                    coins = fetchedCoinPrices
                    val pricesList = fetchedCoinPrices.mapNotNull { it.price }
                    priceCoins = pricesList

                    val fetchedManualCoinPrices = coinRepository.getManualCoinPrices(coinNames)
                    fetchedManualCoinPrices.forEach { coinName ->
                        Log.d("CoinData", "Coin: ${coinName}")
                    }

                } catch (e: Exception) {
                    Log.e("CoinDataError", "${e.message}")
                }
            }
        }

        priceCoins = dbViewModel.getCoinPrices()
        barData = dbViewModel.getCoinDataBar(25)
        barGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks = barData.scaleMarks)
    }



    Scaffold(
        topBar = { topBar() },
        bottomBar = {
            if (isLoggedIn) {
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

            NavHost(navController = navController, startDestination = "createAccount") {

                composable("login") {
                    LoginScreen(
                        viewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
                        navController = navController
                    ) { name, email, password ->
                        userName = name
                        userEmail = email
                        userPassword = password
                        isLoggedIn = true
                        navController.navigate("home")
                    }
                }

                composable("createAccount") {
                    CreateAccountScreen(navController)
                }

                composable("home") {
                    homeScreen(navController = navController)
                }

                composable("writeData") {
                    val context = LocalContext.current
                    val activity = context as? MainActivity

                    VisualizationDataInputScreen(
                        randomClick = {
                            dataSourceType = "Random"
                            return@VisualizationDataInputScreen true
                        },
                        cryptoClick = {
                            dataSourceType = "Crypto"
                            return@VisualizationDataInputScreen true
                        },
                        manualClick = {
                            return@VisualizationDataInputScreen true
                        },
                        csvClick = {
                            return@VisualizationDataInputScreen true
                        },
                        selectedChip = dataSourceType
                    )
                    /*
                    WriteData(
                        navController = navController,
                        dbViewModel = dbViewModel,
                        onCsvSelectClick = { activity?.startCsvFilePicker() }
                    )*/
                }

                composable("profile") {
                    userProfileScreen(
                        navController = navController,
                        onLogout = {
                            isLoggedIn = false
                        }
                    )
                }

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
                    InsertionSortVisualizer(barGraphViewModel = barGraphViewModel)
                }

                composable("Quick Sort") {
                    QuickSortVisualizer(barGraphViewModel = barGraphViewModel)
                }

                composable("Stacks") {
                    val viewModelStack = remember { StackViewModel(priceCoins) }
                    StackVisualizer(viewModelStack)
                }

                composable("Queues") {
                    val viewModelQueue = remember { QueueViewModel(priceCoins) }
                    QueuesVisualizer(viewModelQueue)
                }

                composable("Binary Trees") {
                    BinaryTreesVisualizer(
                        valuesList = priceCoins,
                        onUseManualEntries = { dbViewModel.datosManuales.value.map { it.toDouble() } },
                        onUseCsvEntries = { dbViewModel.datosCsv.value.map { it.toDouble() } }
                    )
                }


                composable("Heaps") {
                    HeapsVisualizer()
                }

                composable("Double Linked Lists") {
                    DoubleLinkedListsVisualizer(priceCoins)
                }
            }
        }
    }
}

fun randomClick(){

}
