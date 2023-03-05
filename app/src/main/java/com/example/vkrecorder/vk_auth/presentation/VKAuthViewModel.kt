package com.example.vkrecorder.vk_auth.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VKAuthViewModel @Inject constructor() : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.ApplicationLoaded -> {
                _authState.value = AuthState.Unauthenticated
            }
            is AuthEvent.AuthenticateViaVK -> {
                _authState.value = AuthState.Authenticated
            }
            is AuthEvent.ContinueAsGuest -> {
                _authState.value = AuthState.AsGuest
            }
        }
    }

}





