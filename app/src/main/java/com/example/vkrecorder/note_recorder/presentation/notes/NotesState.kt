package com.example.vkrecorder.note_recorder.presentation.notes

import com.example.vkrecorder.note_recorder.domain.model.Note
import com.example.vkrecorder.note_recorder.domain.util.NoteOrder
import com.example.vkrecorder.note_recorder.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
