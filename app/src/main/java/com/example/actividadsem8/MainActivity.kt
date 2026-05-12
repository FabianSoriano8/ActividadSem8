package com.example.actividadsem8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.actividadsem8.ui.theme.ActividadSem8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val sessionManager = SessionManager(applicationContext)
        val factory = AuthViewModelFactory(sessionManager)
        
        enableEdgeToEdge()
        setContent {
            ActividadSem8Theme {
                val authViewModel: AuthViewModel = viewModel(factory = factory)
                AppNavigation(viewModel = authViewModel)
            }
        }
    }
}
