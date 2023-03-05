package com.example.vkrecorder.note_recorder.presentation.edit_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun RecordStopButton(
    onStop: () -> Unit,
    onRecord: () -> Unit,
    modifier: Modifier = Modifier
) {

    var buttonClickedState by remember {
        mutableStateOf(true)
    }

    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(RoundedCornerShape(80.dp))
            .background(MaterialTheme.colors.primary)
            .padding(20.dp)
    ) {
        IconButton(
            onClick = {
                when (buttonClickedState) {
                    true -> {
                        onRecord()
                    }
                    else -> {
                        onStop()
                    }
                }
                buttonClickedState = !buttonClickedState
            },
            modifier =  Modifier.fillMaxSize()
                .align(Alignment.Center)
        ) {
            when (buttonClickedState) {
                true -> {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Воспроизвести",
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colors.onPrimary)
                }
                else -> {
                    Icon(
                        Icons.Default.Stop,
                        contentDescription = "Остановить запись",
                        modifier = Modifier.size(120.dp),
                        tint = MaterialTheme.colors.onPrimary)
                }
            }
        }
    }
}