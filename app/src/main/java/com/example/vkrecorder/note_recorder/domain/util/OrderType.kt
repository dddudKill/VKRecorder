package com.example.vkrecorder.note_recorder.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
