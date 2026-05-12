package com.example.actividadsem8

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val loggedIn = sessionManager.isLoggedIn.first()
            val email = sessionManager.userEmail.first()
            _isLoggedIn.value = loggedIn
            _userEmail.value = email
        }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Email y contraseña son requeridos")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        sessionManager.saveSession(email)
                        _isLoggedIn.value = true
                        _userEmail.value = email
                        _authState.value = AuthState.Success
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error al iniciar sesión")
                }
            }
    }

    fun register(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Email y contraseña son requeridos")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewModelScope.launch {
                        sessionManager.saveSession(email)
                        _isLoggedIn.value = true
                        _userEmail.value = email
                        _authState.value = AuthState.Success
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error al registrarse")
                }
            }
    }

    fun logout() {
        auth.signOut()
        viewModelScope.launch {
            sessionManager.clearSession()
            _isLoggedIn.value = false
            _userEmail.value = null
            _authState.value = AuthState.Idle
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
