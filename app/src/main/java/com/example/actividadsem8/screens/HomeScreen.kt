package com.example.actividadsem8.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.actividadsem8.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel) {
    val email by viewModel.userEmail.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Hola ${email ?: "Usuario"}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = { viewModel.logout() }) {
            Text("Cerrar Sesión")
        }
    }
}
