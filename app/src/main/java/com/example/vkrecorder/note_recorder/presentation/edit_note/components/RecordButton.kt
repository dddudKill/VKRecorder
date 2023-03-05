package com.example.vkrecorder.note_recorder.presentation.edit_note.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecordButton(
    modifier: Modifier = Modifier,
    isRecording: Boolean,
    onRecordClick: () -> Unit,
    onStopClick: () -> Unit,
    onPauseClick: () -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isRecording) {
//            IconButton(onPauseClick) {
//                Icon(
//                    imageVector = Icons.Default.PauseCircleFilled,
//                    contentDescription = "Приостановить запись",
//                    modifier = Modifier.size(160.dp),
//                    tint = MaterialTheme.colors.primary
//                )
//            }
            IconButton(onStopClick) {
                Icon(
                    imageVector = Icons.Default.StopCircle,
                    contentDescription = "Закончить запись",
                    modifier = Modifier.size(160.dp),
                    tint = MaterialTheme.colors.primary
                )
            }
        } else {
            IconButton(onRecordClick) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Начать запись",
                    modifier = Modifier.size(160.dp),
                    tint = MaterialTheme.colors.primary
                )
            }
        }
    }
}