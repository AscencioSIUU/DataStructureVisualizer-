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
import com.example.datastructurevisualizerapp.viewmodels.BarGraphViewModel
import com.example.datastructurevisualizerapp.viewmodels.DbViewModel
import com.example.datastructurevisualizerapp.views.InsertionSortVisualizer
import com.example.datastructurevisualizerapp.views.QueueViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.net.Uri
import android.Manifest
import com.example.datastructurevisualizerapp.domain.models.Coin

class MainActivity : ComponentActivity() {

    // Crear una referencia al ViewModel
    private lateinit var dbViewModel: DbViewModel

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permissions", "Permission granted!")
        } else {
            Log.d("Permissions", "Permission denied!")
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
            val barData = dbViewModel.getCoinDataBar(25)
            val barGraphViewModel: BarGraphViewModel = BarGraphViewModel(normalizedBar = barData.normalizedBars, scaleMarks = barData.scaleMarks)
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

                    WriteData(
                        navController = navController,
                        dbViewModel = dbViewModel,
                        onCsvSelectClick = { activity?.startCsvFilePicker() }
                    )
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
                        onUseManualEntries = { dbViewModel.storedNumbers.value.map { it.toDouble() } }
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
