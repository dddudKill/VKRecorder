package com.example.vkrecorder.vk_auth.presentation.components

import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.example.vkrecorder.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vkrecorder.note_recorder.presentation.util.Screen
import com.example.vkrecorder.vk_auth.domain.util.Consts.Companion.SCOPES
import com.example.vkrecorder.vk_auth.presentation.AuthEvent
import com.example.vkrecorder.vk_auth.presentation.AuthState
import com.example.vkrecorder.vk_auth.presentation.VKAuthViewModel
import com.vk.api.sdk.auth.VKScope
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    viewModel: VKAuthViewModel = hiltViewModel(),
    onVKClick: () -> Unit
) {

    val scaffoldState = rememberScaffoldState()
    val authState = viewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState
    ) { it ->

        when (authState.value) {
            is AuthState.Initial -> {
                // Анимация при запуске?
                viewModel.onEvent(AuthEvent.ApplicationLoaded)
            }
            is AuthState.Authenticated -> {
//                LaunchedEffect(Unit) {
//                    navController.popBackStack()
//                    navController.navigate(Screen.NotesScreen.route)
//                }
            }
            is AuthState.AsGuest -> {
//                LaunchedEffect(Unit) {
//                    viewModel.onEvent(AuthEvent.ApplicationLoaded)
//                    navController.navigate(Screen.NotesScreen.route)
//                }
            }
            is AuthState.Unauthenticated -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(0.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.vk_logo_blue_160x160),
                            contentDescription = stringResource(R.string.vk_recorder_description)
                        )
                        Spacer(modifier = Modifier.height(120.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = stringResource(R.string.log_ig_with), style = MaterialTheme.typography.body1)
                            Spacer(modifier = Modifier.height(20.dp))
                            AuthButton {
                                scope.launch {
                                    onVKClick()
                                }
                            }
                        }

                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Продолжить без авторизации",
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier
                                .clickable {
                                    viewModel.onEvent(AuthEvent.ContinueAsGuest)
                                }
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}





