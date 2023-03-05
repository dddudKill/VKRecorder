package com.example.vkrecorder.note_recorder.domain.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
    fun getCurrentPosition(): Int?
    fun isPlaying(): Boolean
}