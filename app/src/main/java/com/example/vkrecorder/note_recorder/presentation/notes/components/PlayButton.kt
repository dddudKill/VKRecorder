package com.example.vkrecorder.note_recorder.presentation.notes.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayButton(isPlaying: Boolean, onClick: () -> Unit) {
    val imageVector = if (isPlaying) {
        Icons.Default.Stop
    } else {
        Icons.Default.PlayArrow
    }
    IconButton(onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = if (isPlaying) "Пауза" else "Воспроизвести",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colors.onPrimary
        )
    }
}
