package com.example.datastructurevisualizerapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.datastructurevisualizerapp.R
import androidx.compose.ui.unit.dp

@Composable
fun WriteData(){
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.insert_data),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.black),
        )
        Text(
            stringResource(R.string.data_examples),
            fontWeight = FontWeight.Thin,
            modifier = Modifier
                .padding(8.dp)
        )

        //OutlinedTextField(value = , onValueChange = )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWriteData(){
    WriteData()
}