package com.example.vkrecorder.note_recorder.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vkrecorder.note_recorder.domain.model.Note

@Database(
    entities = [Note::class],
    version = 4
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}