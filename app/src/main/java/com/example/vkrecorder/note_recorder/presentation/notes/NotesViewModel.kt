package com.example.vkrecorder.note_recorder.presentation.notes

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkrecorder.note_recorder.domain.model.Note
import com.example.vkrecorder.note_recorder.domain.playback.AndroidAudioPlayer
import com.example.vkrecorder.note_recorder.domain.use_cases.note_use_cases.NoteUseCases
import com.example.vkrecorder.note_recorder.domain.util.NoteOrder
import com.example.vkrecorder.note_recorder.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val androidAudioPlayer: AndroidAudioPlayer,
    private val context: Context,
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private var recentlyDeletedNote: Note? = null

    private var recentlyDeletedNoteRecordFile: File? = null

    private val _currentTime = MutableStateFlow<Long?>(null)
    val currentTime: StateFlow<Long?> = _currentTime

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    recentlyDeletedNote = event.note
                    val outputDir = context.cacheDir
                    recentlyDeletedNoteRecordFile = File.createTempFile("recording_backup", ".3gp", outputDir)
                    File(event.note.path).copyTo(recentlyDeletedNoteRecordFile!!, overwrite = true)
                    File(event.note.path).delete()
                    noteUseCases.deleteNote(event.note)
                }
            }
            is NotesEvent.PlayNote -> {
                androidAudioPlayer.playFile(File(event.note.path))
                viewModelScope.launch {
                    while (androidAudioPlayer.isPlaying()) {
                        _currentTime.value = androidAudioPlayer.getCurrentPosition()?.toLong()
                        delay(500)
                    }
                    androidAudioPlayer.stop()
                    _currentTime.value = null
                }
            }
            is NotesEvent.StopPlayingNote -> {
                androidAudioPlayer.stop()
                //_currentTime.value = null
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNoteRecordFile?.copyTo(File(recentlyDeletedNote!!.path))
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }
}