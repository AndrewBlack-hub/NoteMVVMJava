package com.example.notemvvmjava;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notemvvmjava.adapter.NotesAdapter;
import com.example.notemvvmjava.model.Note;
import com.example.notemvvmjava.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainFragment extends Fragment {

    private NotesAdapter adapter = new NotesAdapter();
    private MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        RecyclerView recyclerViewListNotes = view.findViewById(R.id.recyclerViewMainFragment);
        recyclerViewListNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewListNotes.setAdapter(adapter);
        viewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notesFromLiveData) {
                adapter.submitList(notesFromLiveData);
                if (notesFromLiveData.size() > 0) {
                    view.findViewById(R.id.imageViewEmptyNote).setVisibility(View.GONE);
                } else {
                    view.findViewById(R.id.imageViewEmptyNote).setVisibility(View.VISIBLE);
                }
            }
        });


        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Navigation.findNavController(view).navigate(R.id.create_note_dest, viewModel.createBundleForNote(note));
            }

            @Override
            public void onNoteLongClick(Note note) {
                final Note localNote = viewModel.createLocaleNote(note);
                viewModel.deleteNote(note);
                Snackbar snackbar = Snackbar.make(view, R.string.restore_a_note, Snackbar.LENGTH_LONG)
                        .setAction(R.string.recover, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewModel.insertNote(localNote);
                                Snackbar.make(view, R.string.note_restored, Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
                snackbar.show();
            }
        });

        final FloatingActionButton fab = view.findViewById(R.id.createNoteBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.create_note_dest);
            }
        });

        recyclerViewListNotes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                } else if (dy < 0) {
                    fab.show();
                }
            }
        });
    }
}