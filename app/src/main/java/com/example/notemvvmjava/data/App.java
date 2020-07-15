package com.example.notemvvmjava.data;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {
    private static App instance;

    private static   NotesDatabase database;
    private static final String DB_NAME = "notes.db";

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(getApplicationContext(), NotesDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public NotesDatabase getDatabase() {
        return database;
    }

    public void setDatabase(NotesDatabase database) {
        this.database = database;
    }
}
