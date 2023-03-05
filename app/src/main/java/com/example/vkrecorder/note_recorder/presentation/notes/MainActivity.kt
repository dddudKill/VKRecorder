package com.example.vkrecorder.note_recorder.presentation.notes

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.app.ActivityCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vkrecorder.note_recorder.presentation.edit_note.components.EditNoteScreen
import com.example.vkrecorder.note_recorder.presentation.notes.components.NotesScreen
import com.example.vkrecorder.note_recorder.presentation.util.Screen
import com.example.vkrecorder.ui.theme.VKRecorderTheme
import com.example.vkrecorder.vk_auth.presentation.WelcomeActivity
import com.example.vkrecorder.vk_auth.presentation.components.AuthScreen
import com.vk.api.sdk.VK
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VKRecorderTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route
                    ) {
                        composable(route = Screen.NotesScreen.route) {
                            NotesScreen(
                                navController = navController,
                                onLogOut = {
                                    VK.logout()
                                    WelcomeActivity.startFrom(this@MainActivity)
                                    finish()
                                }
                            )
                        }
                        composable(
                            route = Screen.EditNoteScreen.route + "?noteId={noteId}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            EditNoteScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
