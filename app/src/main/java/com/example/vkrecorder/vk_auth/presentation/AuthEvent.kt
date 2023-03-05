package com.example.vkrecorder.vk_auth.presentation

sealed class AuthEvent {
    object ApplicationLoaded: AuthEvent()
    object AuthenticateViaVK: AuthEvent()
    object ContinueAsGuest: AuthEvent()
}