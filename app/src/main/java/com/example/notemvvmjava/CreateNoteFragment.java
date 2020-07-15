package com.example.notemvvmjava;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notemvvmjava.model.Note;
import com.example.notemvvmjava.viewmodel.CreateNoteViewModel;
import com.example.notemvvmjava.viewmodel.MainViewModel;

public class CreateNoteFragment extends Fragment implements ICreateNoteView {

    private CreateNoteViewModel viewModelCreateNote;
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewDate;
    private Note note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_note, container, false);
        viewModelCreateNote = new ViewModelProvider(this).get(CreateNoteViewModel.class);
        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        textViewDate = view.findViewById(R.id.textViewDateOfChangeNote);

        Bundle bundle = getArguments();
        if (bundle != null) {
            note = bundle.getParcelable(MainViewModel.BUNDLE_KEY);
            assert (note != null);
            initComponents(note);
            textViewDate.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        if (item.getItemId() == R.id.action_save_note) {
            if (note != null) {
                if (viewModelCreateNote.validation(title, description)) {
                    viewModelCreateNote.updateNote(new Note(note.getId(), title, description, viewModelCreateNote.date()));
                    goHome();
                } else {
                    showMsgFailValid();
                }
            } else {
                if (viewModelCreateNote.validation(title, description)) {
                    viewModelCreateNote.clickSaveNote(title, description);
                    showSuccessful();
                    goHome();
                } else {
                    showMsgFailValid();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showMsgFailValid() {
        Toast.makeText(getContext(), R.string.enter_all_fields, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSuccessful() {
        Toast.makeText(getContext(), R.string.successful_saving, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initComponents(Note note) {
        editTextTitle.setText(note.getTitle());
        editTextDescription.setText(note.getDescription());
        textViewDate.setText(String.format(getResources().getString(R.string.date_of_update), note.getDate()));
    }

    public Note startNote() {
        return note;
    }

    @Override
    public Note newNoteForEquals() {
        Note newNoteForEquals = new Note();
        newNoteForEquals.setId(note.getId());
        newNoteForEquals.setTitle(editTextTitle.getText().toString());
        newNoteForEquals.setDescription(editTextDescription.getText().toString());
        newNoteForEquals.setDate(note.getDate());
        return newNoteForEquals;
    }

    @Override
    public String getTitleFromEditText() {
        return editTextTitle.getText().toString();
    }

    @Override
    public String getDescriptionFromEditText() {
        return editTextTitle.getText().toString();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (note != null) {
                    if (!(newNoteForEquals().equals(startNote()))) {
                        viewModelCreateNote.createAlertDialog(getContext());
                    } else {
                        goHome();
                    }
                } else {
                    goHome();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void goHome() {
        Navigation.findNavController(requireView()).navigate(R.id.home_dest);
    }
}