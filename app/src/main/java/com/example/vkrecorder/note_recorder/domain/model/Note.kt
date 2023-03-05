package com.example.vkrecorder.note_recorder.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val content: String = "",
    val length: Long,
    val timestamp: Long,
    var isPlaying: Boolean = false,
    val path: String
)

class InvalidNoteException(message: String): Exception(message)
