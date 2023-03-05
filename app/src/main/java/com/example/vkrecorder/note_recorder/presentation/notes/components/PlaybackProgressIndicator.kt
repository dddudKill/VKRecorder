package com.example.vkrecorder.note_recorder.presentation.notes.components

import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun PlaybackProgressIndicator(
    modifier: Modifier = Modifier,
    currentTime: Long,
    duration: Long
) {
    val progress = remember(currentTime, duration) {
        currentTime.toFloat() / duration.toFloat()
    }
    LinearProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.surface
    )
}
