package com.example.vkrecorder.note_recorder.presentation.util

sealed class Screen(val route: String) {
    object NotesScreen: Screen("notes_screen")
    object EditNoteScreen: Screen("edit_note_screen")
    object AuthScreen: Screen("auth_screen")
}