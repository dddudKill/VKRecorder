package com.example.vkrecorder.note_recorder.domain.use_cases.note_use_cases

import com.example.vkrecorder.note_recorder.domain.model.Note
import com.example.vkrecorder.note_recorder.domain.repository.NoteRepository
import com.example.vkrecorder.note_recorder.domain.util.NoteOrder
import com.example.vkrecorder.note_recorder.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val repository: NoteRepository
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when(noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Length -> notes.sortedBy { it.length }
                    }
                }
                is OrderType.Descending -> {
                    when(noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Length -> notes.sortedByDescending { it.length }
                    }
                }
            }
        }
    }

}