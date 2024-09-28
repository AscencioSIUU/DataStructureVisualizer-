package com.example.datastructurevisualizerapp.screens

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.res.colorResource
import com.example.datastructurevisualizerapp.R
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun userProfileScreen(
    user: String,
    email: String,
    password: String,
){
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    val ovalColor = colorResource(id = R.color.mainColor)


    val isFormValid = userName.isNotBlank() && userEmail.isNotBlank() && userPassword.isNotBlank()

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.user_profile),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.mainColor),
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Box(
            modifier = Modifier
                .size(140.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(100.dp)){
                drawOval(
                    color = ovalColor,
                    size = this.size
                )
            }
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                tint = colorResource(R.color.white),
                modifier = Modifier.size(110.dp),
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = user,
            onValueChange = { /*TODO*/},
            label = { Text("Nombre") },
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { /*TODO*/},
            label = { Text("Email") },
        )
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { /*TODO*/},
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Contraseña") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.size(32.dp))
        FilledTonalButton(
            onClick = { /*TODO*/ },
            enabled = isFormValid,
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (isFormValid) MaterialTheme.colorScheme.secondary else Color.Gray,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Guardar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserProfileScreen(){
    userProfileScreen(user = "", email = "", password = "")
}