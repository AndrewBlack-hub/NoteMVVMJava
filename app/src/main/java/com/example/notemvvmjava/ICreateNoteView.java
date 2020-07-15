package com.example.notemvvmjava;

import com.example.notemvvmjava.model.Note;

public interface ICreateNoteView {
    void goHome();
    String getTitleFromEditText();
    String getDescriptionFromEditText();
    void showMsgFailValid();
    void showSuccessful();
    void initComponents(Note note);
    Note newNoteForEquals();
}
