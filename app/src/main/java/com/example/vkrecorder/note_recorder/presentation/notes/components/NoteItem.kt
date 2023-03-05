package com.example.vkrecorder.note_recorder.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.vkrecorder.R
import com.example.vkrecorder.note_recorder.domain.model.Note
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    currentlyPlaying: Note?,
    onDeleteClick: () -> Unit,
    onPlayClick: (Note?) -> Unit,
    currentTime: Long? = null
) {

    val isPlaying = note == currentlyPlaying
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                //.clip(RoundedCornerShape(cornerRadius))
                .background(MaterialTheme.colors.surface)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .width(120.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = note.title,
                            style = MaterialTheme.typography.h2
                        )
                        IconButton(
                            onClick = onDeleteClick
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete),
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colors.primary)
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        val minutes = note.length / 1000 / 60
                        val seconds = note.length / 1000 % 60
                        val formattedTime = String.format(stringResource(R.string.time_format), minutes, seconds)

                        if (isPlaying && currentTime != null) {
                            val minutesCurrent = currentTime / 1000 / 60
                            val secondsCurrent = currentTime / 1000 % 60

                            val formattedCurrentTime = String.format(stringResource(R.string.time_format), minutesCurrent, secondsCurrent)

                            Text("$formattedCurrentTime / ", style = MaterialTheme.typography.body1)
                        }
                        Text(text = formattedTime, style = MaterialTheme.typography.body2)
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .background(MaterialTheme.colors.primary)
                                .padding(8.dp)
                        ) {
                            PlayButton(
                                isPlaying = isPlaying && currentTime != null,
                                onClick = {
                                    if (isPlaying) {
                                        note.isPlaying = false
                                        onPlayClick(null)
                                    } else {
                                        currentlyPlaying?.isPlaying = false
                                        note.isPlaying = true
                                        onPlayClick(note)
                                    }
                                }
                            )
                        }
                    }
                }
                val date = Date(note.timestamp)
                val dateFormat = if (isToday(date)) {
                    SimpleDateFormat(stringResource(R.string.today_date_format) ,Locale("ru", "RU"))
                } else if (isYesterday(date)) {
                    SimpleDateFormat(stringResource(R.string.yesterday_date_format), Locale("ru", "RU"))
                } else {
                    SimpleDateFormat(stringResource(R.string.any_date_format), Locale("ru", "RU"))
                }
                Text(text = dateFormat.format(date), style = MaterialTheme.typography.body2)
            }
        }
        currentTime?.let {
            if (isPlaying) PlaybackProgressIndicator(modifier = Modifier.fillMaxWidth(), currentTime = it, duration = note.length)
        }
    }
}

fun isToday(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val today = calendar.time
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    val yesterday = calendar.time
    return date >= yesterday && date < today
}

fun isYesterday(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    val tomorrow = calendar.time
    calendar.add(Calendar.DAY_OF_MONTH, -2)
    val yesterday = calendar.time
    val today = Date()

    return today.after(yesterday) && today.before(tomorrow)
}