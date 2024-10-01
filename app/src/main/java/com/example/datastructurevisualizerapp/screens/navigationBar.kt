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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.datastructurevisualizerapp.R

@Composable
fun NavigationBar(
    editColor: Int,
    homeColor: Int,
    profileColor: Int,
    navController: NavController,
    user: String,
    email: String,
    password: String
) {
    val editColor = if (editColor == 1) Color(0xFF32CD32) else Color(0xFFFFFFFF)
    val homeColor = if (homeColor == 1) Color(0xFF32CD32) else Color(0xFFFFFFFF)
    val profileColor = if (profileColor == 1) Color(0xFF32CD32) else Color(0xFFFFFFFF)
    Row (
        modifier = Modifier
            .background(colorResource(id = R.color.mainColor))
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
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
                onClick = { navController.navigate("writeData") }
            ) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = null,
                    tint = editColor
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
                onClick = {navController.navigate("home/$user/$email/$password")}
            ) {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = null,
                    tint = homeColor
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
                onClick = {navController.navigate("profile/$user/$email/$password")}
            ){
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = profileColor
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

