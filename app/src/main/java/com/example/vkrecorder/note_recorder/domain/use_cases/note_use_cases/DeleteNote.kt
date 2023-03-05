package com.example.vkrecorder.note_recorder.domain.use_cases.note_use_cases

import com.example.vkrecorder.note_recorder.domain.model.Note
import com.example.vkrecorder.note_recorder.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}