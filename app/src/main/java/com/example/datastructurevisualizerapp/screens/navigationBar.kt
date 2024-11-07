package com.example.datastructurevisualizerapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.datastructurevisualizerapp.R

@Composable
fun NavigationBar(
    navController: NavController,
    user: String,
    email: String,
    password: String
) {
    var selectedItem by remember { mutableStateOf(1) }

    fun getItemColor(itemIndex: Int): Color {
        return if(selectedItem == itemIndex) Color(0xFF32CD32) else Color(0xFFFFFFFF)
    }

    Row (
        modifier = Modifier
            .background(colorResource(id = R.color.mainColor))
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        //Write data icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(modifier = Modifier.size(100.dp)) {
                drawOval(
                    color = Color(0xFF4B5563),
                    size = this.size
                )
            }
            IconButton(
                onClick = {
                    selectedItem = 0
                    navController.navigate("writeData")
                }
            ) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = null,
                    tint = getItemColor(0)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(modifier = Modifier.size(100.dp)) {
                drawOval(
                    color = Color(0xFF4B5563),
                    size = this.size
                )
            }
            IconButton(
                onClick = {
                    selectedItem = 1
                    navController.navigate("dataSelection")
                }
            ) {
                Icon(
                    Icons.Filled.Build,
                    contentDescription = null,
                    tint = getItemColor(1)
                )
            }
        }
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(modifier = Modifier.size(100.dp)) {
                drawOval(
                    color = Color(0xFF4B5563),
                    size = this.size
                )
            }
            IconButton(
                onClick = {
                    selectedItem = 2
                    navController.navigate("home")
                }
            ) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = null,
                    tint = getItemColor(2)
                )
            }
        }
        //Profile Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ){
            Canvas(modifier = Modifier.size(100.dp)) {
                drawOval(
                    color = Color(0xFF4B5563),
                    size = this.size
                )
            }
            IconButton(
                onClick = {
                    selectedItem = 3
                    navController.navigate("profile")                }
            ){
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = getItemColor(3)
                )

            }
        }
    }
}

@Composable
fun topBar(){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.mainColor))
            .height(100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(R.string.app_main_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.white),
            modifier = Modifier
                .padding(bottom = 30.dp)
        )
    }

}

