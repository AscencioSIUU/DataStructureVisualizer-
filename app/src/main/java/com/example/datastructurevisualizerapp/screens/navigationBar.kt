package com.example.datastructurevisualizerapp.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBar(modifier: Modifier){
    Row (
        modifier = Modifier.padding(8.dp)
    ) {

        Icon(Icons.Filled.Create, contentDescription = null)

        Icon(Icons.Filled.Home, contentDescription = null)

        Icon(Icons.Filled.AccountCircle, contentDescription = null)
    }
}

@Preview
@Composable
fun PreviewNavigationBar(){
    NavigationBar(modifier = Modifier)
}