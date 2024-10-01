package com.example.datastructurevisualizerapp


import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.datastructurevisualizerapp.screens.CreateAccountScreen
import com.example.datastructurevisualizerapp.screens.LoginScreen
import com.example.datastructurevisualizerapp.screens.NavigationBar
import com.example.datastructurevisualizerapp.screens.topBar
import com.example.datastructurevisualizerapp.screens.homeScreen
import com.example.datastructurevisualizerapp.ui.theme.DataStructureVisualizerAppTheme

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

    var isLoggedIn by remember { mutableStateOf(false) }
    val navController = rememberNavController() //controlador de la navegación

    Scaffold(
        topBar = {
            topBar()
        },

        bottomBar = {
            if (isLoggedIn){
                NavigationBar(editColor = 0, homeColor = 1, profileColor = 0)
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
                    LoginScreen(navController)
                }
                composable("home") {
                    homeScreen(navController)
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