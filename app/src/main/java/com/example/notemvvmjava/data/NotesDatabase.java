package com.example.notemvvmjava.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notemvvmjava.model.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NotesDao notesDao();
}
