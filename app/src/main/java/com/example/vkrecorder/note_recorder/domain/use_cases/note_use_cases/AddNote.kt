package com.example.vkrecorder.note_recorder.domain.use_cases.note_use_cases

import com.example.vkrecorder.note_recorder.domain.model.InvalidNoteException
import com.example.vkrecorder.note_recorder.domain.model.Note
import com.example.vkrecorder.note_recorder.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("Название записи не может быть пустым")
        }
        repository.insertNote(note)
    }
}