package com.example.notemvvmjava.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.example.notemvvmjava.R;
import com.example.notemvvmjava.data.App;
import com.example.notemvvmjava.data.NotesDao;
import com.example.notemvvmjava.model.Note;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateNoteViewModel extends ViewModel {

    private NotesDao notesDao = App.getInstance().getDatabase().notesDao();

    public boolean validation(String title, String description) {
        return !(title.isEmpty() || description.isEmpty());
    }

    public void clickSaveNote(String title, String description) {
        Note note = new Note(title, description, date());
        saveNote(note);
    }

    public void createAlertDialog(final Context context, final String currentTitle,
                                  final String currentDescription, final Note newNoteForEquals,
                                  final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(R.string.save_the_changes)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (validation(currentTitle, currentDescription)) {
                            updateNote(newNoteForEquals);

                            Navigation.findNavController(view).navigate(R.id.home_dest);
                        } else {
                            Toast.makeText(context, R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Navigation.findNavController(view).navigate(R.id.home_dest);
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
