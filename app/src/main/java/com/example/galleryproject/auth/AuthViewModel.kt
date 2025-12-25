package com.example.galleryproject.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryproject.data.auth.AuthRepository
import com.example.galleryproject.models.Client
import com.example.galleryproject.session.UserSession
import com.example.galleryproject.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userSession: UserSession
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Idle)
    val loginState: LiveData<UiState<Unit>> = _loginState

    private val _registerState = MutableLiveData<UiState<Unit>>(UiState.Idle)
    val registerState: LiveData<UiState<Unit>> = _registerState

    fun login(email: String, password: String) {
        _loginState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                userSession.saveToken(response.accessToken)
                userSession.saveUser(
                    Client(
                        username = email.substringBefore("@"),
                        email = email
                    )
                )
                _loginState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _loginState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun register(request: RegisterRequest) {
        _registerState.value = UiState.Loading
        viewModelScope.launch {
            try {
                authRepository.register(request)
                _registerState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _registerState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loginAsGuest() {
        userSession.saveUser(Client(username = "Guest", email = ""))
        _loginState.value = UiState.Success(Unit)
    }
}
