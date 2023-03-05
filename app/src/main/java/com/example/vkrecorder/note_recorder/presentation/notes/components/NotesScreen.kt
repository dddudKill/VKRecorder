package com.example.vkrecorder.note_recorder.presentation.notes.components

import android.content.res.Resources
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vkrecorder.R
import com.example.vkrecorder.note_recorder.domain.model.Note
import com.example.vkrecorder.note_recorder.presentation.notes.NotesEvent
import com.example.vkrecorder.note_recorder.presentation.notes.NotesViewModel
import com.example.vkrecorder.note_recorder.presentation.util.Screen
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel(),
    onLogOut: () -> Unit
) {
    val state = viewModel.state.value
    val currentTime = viewModel.currentTime.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var currentlyPlaying by remember {
        mutableStateOf<Note?>(null)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.EditNoteScreen.route)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_recording)
                )
            }
        },
        scaffoldState = scaffoldState
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.your_recordings),
                    style = MaterialTheme.typography.h1
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onLogOut,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = stringResource(R.string.log_out)
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.onEvent(NotesEvent.ToggleOrderSection)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = stringResource(R.string.filters)
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes, key = { it.hashCode() }) { note ->
                    if (!File(note.path).exists()) {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                    }
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.EditNoteScreen.route +
                                            "?noteId=${note.id}"
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = scaffoldState.snackbarHostState.showSnackbar(
                                    message = Resources.getSystem().getString(R.string.recording_deleted),
                                    actionLabel = Resources.getSystem().getString(R.string.cancel)
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        },
                        currentlyPlaying = currentlyPlaying,
                        onPlayClick = { newPlayingRecording ->
                            currentlyPlaying?.isPlaying = false
                            currentlyPlaying = newPlayingRecording
                            if (currentlyPlaying != null) viewModel.onEvent(
                                NotesEvent.PlayNote(
                                    currentlyPlaying!!
                                )
                            ) else viewModel.onEvent(NotesEvent.StopPlayingNote)
                        },
                        currentTime = currentTime.value
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}