package com.example.vkrecorder.note_recorder.presentation.edit_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vkrecorder.R
import com.example.vkrecorder.note_recorder.presentation.edit_note.EditNoteEvent
import com.example.vkrecorder.note_recorder.presentation.edit_note.EditNoteViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditNoteScreen(
    navController: NavController,
    viewModel: EditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scaffoldState = rememberScaffoldState()

    var buttonClickedState by remember {
        mutableStateOf(true)
    }

    var isRecording by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is EditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is EditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    buttonClickedState = true
                    if (isRecording) viewModel.onEvent(EditNoteEvent.NoteRecordingStopped)
                    viewModel.onEvent(EditNoteEvent.SaveNote)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = stringResource(R.string.save))
            }
        },
        scaffoldState = scaffoldState
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TransparentHintTextField(
                    text = titleState.text,
                    hint = titleState.hint,
                    onValueChange = {
                        viewModel.onEvent(EditNoteEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(EditNoteEvent.ChangeTitleFocus(it))
                    },
                    isHintVisible = titleState.isHintVisible,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = contentState,
                    style = MaterialTheme.typography.body1,
                   // modifier = Modifier.fillMaxHeight()
                )
            }
            RecordButton(
                isRecording = isRecording,
                onRecordClick = {
                    viewModel.onEvent(EditNoteEvent.NoteRecordingStarted)
                    isRecording = !isRecording
                },
                onStopClick = {
                    viewModel.onEvent(EditNoteEvent.NoteRecordingStopped)
                    isRecording = !isRecording
                },
                onPauseClick = {
                    viewModel.onEvent(EditNoteEvent.NoteRecordingPaused)
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}