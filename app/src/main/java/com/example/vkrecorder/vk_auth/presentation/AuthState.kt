package com.example.vkrecorder.vk_auth.presentation

sealed class AuthState {
    object Initial : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object AsGuest: AuthState()
}