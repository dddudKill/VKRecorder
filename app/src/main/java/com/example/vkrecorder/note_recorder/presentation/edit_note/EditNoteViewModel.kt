package com.example.vkrecorder.note_recorder.presentation.edit_note

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkrecorder.note_recorder.domain.model.InvalidNoteException
import com.example.vkrecorder.note_recorder.domain.model.Note
import com.example.vkrecorder.note_recorder.domain.use_cases.note_use_cases.NoteUseCases
import com.example.vkrecorder.note_recorder.domain.record.AndroidAudioRecorder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class EditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val androidAudioRecorder: AndroidAudioRecorder,
    private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Введите название"
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    var currentRecordingPath: String? = null
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

//  Контент для распознавания речи
    private val _noteContent = mutableStateOf("")
    val noteContent: State<String> = _noteContent

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?. also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value
                    }
                }
            }
        }
    }

    fun onEvent(event: EditNoteEvent) {
        when(event) {
            is EditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is EditNoteEvent.NoteRecordingStarted -> {
                val outputDir = context.cacheDir
                val outputFile = File.createTempFile("recording", ".3gp", outputDir)
                currentRecordingPath = outputFile.absolutePath
                androidAudioRecorder.start(outputFile)
            }
            is EditNoteEvent.NoteRecordingStopped -> {
                androidAudioRecorder.stop()
            }
            is EditNoteEvent.NoteRecordingPaused -> {
                androidAudioRecorder
            }
            is EditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is EditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        val currentTimestamp = System.currentTimeMillis()
                        val fileName = "${noteTitle.value.text}_${currentTimestamp}.3gp"
                        val recordingsDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath
                        val recordingFile = File(recordingsDir, fileName)

                        val cacheFile = File(currentRecordingPath ?: throw InvalidNoteException("Вы не сделали запись"))
                        if (cacheFile.exists()) {
                            cacheFile.copyTo(recordingFile, overwrite = true)
                            cacheFile.delete()
                        }

                        currentRecordingPath = recordingFile.absolutePath


                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                timestamp = currentTimestamp,
                                length = androidAudioRecorder.getRecordedAudioFileLength(currentRecordingPath ?: throw InvalidNoteException("Вы не сделали запись")),
                                id = currentNoteId,
                                path = currentRecordingPath ?: throw InvalidNoteException("Вы не сделали запись")
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Не удалось сохранить запись"
                            )
                        )
                    }
                }
            }
        }

    }


    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}