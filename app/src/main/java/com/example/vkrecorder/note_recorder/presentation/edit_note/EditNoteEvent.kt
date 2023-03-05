package com.example.vkrecorder.note_recorder.presentation.edit_note

import androidx.compose.ui.focus.FocusState

sealed class EditNoteEvent {
    data class EnteredTitle(val value: String): EditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): EditNoteEvent()
    object NoteRecordingStarted: EditNoteEvent()
    object NoteRecordingStopped: EditNoteEvent()
    object NoteRecordingPaused: EditNoteEvent()
    object SaveNote: EditNoteEvent()
}