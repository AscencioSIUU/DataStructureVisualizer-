package com.example.datastructurevisualizerapp


import android.os.Bundle
import android.text.Selection
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.datastructurevisualizerapp.views.BarGraphViewModel
import com.example.datastructurevisualizerapp.screens.CreateAccountScreen
import com.example.datastructurevisualizerapp.screens.LoginScreen
import com.example.datastructurevisualizerapp.screens.NavigationBar
import com.example.datastructurevisualizerapp.views.StackViewModel
import com.example.datastructurevisualizerapp.views.StackVisualizer
import com.example.datastructurevisualizerapp.views.TestingBarGraph
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataStructureVisualizerAppTheme {
                MyDataStructureVisualizerApp()
            }
        }
    }
}

@Composable
fun MyDataStructureVisualizerApp() {

    var isLoggedIn by rememberSaveable  { mutableStateOf(false) }
    val navController = rememberNavController() //controlador de la navegación
    var userName by rememberSaveable { mutableStateOf("") }
    var userEmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }

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

            NavHost( navController = navController, startDestination = "login") {
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
                    MergeSortVisualizer()
                }

                composable("Bubble Sort") {
                    BubbleSortVisualizer()
                }

                composable("Selection Sort") {
                    SelectionSortVisualizer()
                }


                composable("Insertion Sort") {
                    val barGraphViewModel = BarGraphViewModel()
                    TestingBarGraph(barGraphViewModel = barGraphViewModel)

                }

                composable("Quick Sort") {
                    QuickSortVisualizer()
                }

                composable("Stacks") {
                    val stackViewModel: StackViewModel = viewModel()
                    StackVisualizer(viewModel = stackViewModel)
                }

                composable("Queues") {
                    QueuesVisualizer()
                }

                composable("Binary Trees") {
                    BinaryTreesVisualizer()
                }

                composable("Heaps") {
                    HeapsVisualizer()
                }

                composable("Double Linked Lists") {
                    DoubleLinkedListsVisualizer()
                }






            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewMyApp() {
    MyDataStructureVisualizerApp()
}