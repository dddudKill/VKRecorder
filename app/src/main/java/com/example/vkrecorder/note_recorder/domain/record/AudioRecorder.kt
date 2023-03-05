package com.example.vkrecorder.note_recorder.domain.record

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
    fun getRecordedAudioFileLength(outputFilePath: String): Long
}