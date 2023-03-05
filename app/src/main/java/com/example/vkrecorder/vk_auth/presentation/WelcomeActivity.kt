package com.example.vkrecorder.vk_auth.presentation

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.app.ActivityCompat
import com.example.vkrecorder.R
import com.example.vkrecorder.note_recorder.presentation.notes.MainActivity
import com.example.vkrecorder.ui.theme.VKRecorderTheme
import com.example.vkrecorder.vk_auth.domain.util.Consts.Companion.SCOPES
import com.example.vkrecorder.vk_auth.presentation.components.AuthScreen
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException

class WelcomeActivity : ComponentActivity() {

    private lateinit var authLauncher: ActivityResultLauncher<Collection<VKScope>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET),
            0
        )

        if (VK.isLoggedIn()) {
            MainActivity.startFrom(this)
            finish()
            return
        }

        authLauncher = VK.login(this) { result : VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> onLogin()
                is VKAuthenticationResult.Failed -> onLoginFailed(result.exception)
            }
        }

        setContent {
            VKRecorderTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Log.d("welcome", "Welcome")
                    AuthScreen(
                        onVKClick = {
                            authLauncher.launch(SCOPES)
                        }
                    )
                }
            }
        }
    }

    private fun onLogin() {
        MainActivity.startFrom(this@WelcomeActivity)
        finish()
    }

    private fun onLoginFailed(exception: VKAuthException) {
        if (!exception.isCanceled) {
            val descriptionResource =
                if (exception.webViewError == WebViewClient.ERROR_HOST_LOOKUP) R.string.message_connection_error
                else R.string.message_unknown_error
            AlertDialog.Builder(this@WelcomeActivity)
                .setMessage(descriptionResource)
                .setPositiveButton(R.string.vk_retry) { _, _ ->
                    authLauncher.launch(arrayListOf(VKScope.WALL, VKScope.PHOTOS))
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}