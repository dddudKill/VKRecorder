package com.example.vkrecorder.note_recorder.domain.util



sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class Date(orderType: OrderType): NoteOrder(orderType)
    class Length(orderType: OrderType): NoteOrder(orderType)

    fun cope(orderType: OrderType): NoteOrder {
        return when(this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
            is Length -> Length(orderType)
        }
    }
}
