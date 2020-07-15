package com.example.notemvvmjava.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.lifecycle.ViewModel;

import com.example.notemvvmjava.ICreateNoteView;
import com.example.notemvvmjava.R;
import com.example.notemvvmjava.data.App;
import com.example.notemvvmjava.data.NotesDao;
import com.example.notemvvmjava.model.Note;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNoteViewModel extends ViewModel {

    ICreateNoteView view;
    private NotesDao notesDao = App.getInstance().getDatabase().notesDao();

    private CreateNoteViewModel(ICreateNoteView view) {
        this.view = view;
    }

    public boolean validation(String title, String description) {
        return !(title.isEmpty() || description.isEmpty());
    }

    public void clickSaveNote(String title, String description) {
        Note note = new Note(title, description, date());
        saveNote(note);
    }

    public void createAlertDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(R.string.save_the_changes)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (validation(view.getTitleFromEditText(),
                                view.getDescriptionFromEditText())) {
                            updateNote(view.newNoteForEquals());
                            view.goHome();
                        } else {
                            view.showMsgFailValid();
                        }
                    }
                });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                view.goHome();
            }
        }).create().show();
    }

    public void saveNote(Note note) {
        notesDao.insertNote(note);
    }

    public void updateNote(Note note) {
        notesDao.updateNote(note);
    }

    public String date() {
        Date currentDate = Calendar.getInstance().getTime();
        return DateFormat.getDateTimeInstance().format(currentDate);
    }
}
