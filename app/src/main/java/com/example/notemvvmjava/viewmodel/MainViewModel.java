package com.example.notemvvmjava.viewmodel;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.notemvvmjava.CreateNoteFragment;
import com.example.notemvvmjava.data.App;
import com.example.notemvvmjava.data.NotesDao;
import com.example.notemvvmjava.model.Note;

import java.util.List;

public class MainViewModel extends ViewModel {

    private NotesDao notesDao = App.getInstance().getDatabase().notesDao();
    public static final String BUNDLE_KEY = "note";

    public LiveData<List<Note>> getData() {
        return notesDao.getAllNotes();
    }

    public Bundle createBundleForNote(Note note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, note);
        CreateNoteFragment fragment = new CreateNoteFragment();
        fragment.setArguments(bundle);
        return bundle;
    }

    public Note createLocaleNote(Note note) {
        return new Note(note.getId(), note.getTitle(), note.getDescription(), note.getDate());
    }

    public void deleteNote(Note note) {
        notesDao.deleteNote(note);
    }

    public void insertNote(Note note) {
        notesDao.insertNote(note);
    }
}
