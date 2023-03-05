package com.example.vkrecorder.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.vkrecorder.note_recorder.data.data_source.NoteDatabase
import com.example.vkrecorder.note_recorder.data.repository.NoteRepositoryImpl
import com.example.vkrecorder.note_recorder.domain.playback.AndroidAudioPlayer
import com.example.vkrecorder.note_recorder.domain.repository.NoteRepository
import com.example.vkrecorder.note_recorder.domain.use_cases.*
import com.example.vkrecorder.note_recorder.domain.record.AndroidAudioRecorder
import com.example.vkrecorder.note_recorder.domain.use_cases.note_use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return  NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideAndroidAudioRecorder(context: Context): AndroidAudioRecorder {
        return  AndroidAudioRecorder(context = context)
    }

    @Provides
    @Singleton
    fun provideAndroidAudioPlayer(context: Context): AndroidAudioPlayer {
        return  AndroidAudioPlayer(context = context)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCases {
        return  NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}