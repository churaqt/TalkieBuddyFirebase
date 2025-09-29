package edu.uph.m2si1.talkiebuddy;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import edu.uph.m2si1.talkiebuddy.model.ProgressNote;
import io.realm.Realm;

public class EditNoteDialog extends DialogFragment {

    private final ProgressNote note;
    private final Runnable onNoteUpdated;

    public EditNoteDialog(ProgressNote note, Runnable onNoteUpdated) {
        this.note = note;
        this.onNoteUpdated = onNoteUpdated;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_note, null);

        EditText edtDate = view.findViewById(R.id.edtDate);
        EditText edtTopic = view.findViewById(R.id.edtTopic);
        EditText edtWords = view.findViewById(R.id.edtWords);
        EditText edtSummary = view.findViewById(R.id.edtSummary);
        EditText edtConfidence = view.findViewById(R.id.edtConfidence);
        EditText edtDuration = view.findViewById(R.id.edtDuration);
        EditText edtNotes = view.findViewById(R.id.edtNotes);
        Button btnSave = view.findViewById(R.id.btnSave);


        if (note != null) {
            edtDate.setText(note.getDate());
            edtTopic.setText(note.getTopic());
            edtWords.setText(note.getWords());
            edtSummary.setText(note.getSummary());
            edtConfidence.setText(String.valueOf(note.getConfidence()));
            edtDuration.setText(note.getDuration());
            edtNotes.setText(note.getNotes());
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();

        btnSave.setText("Update");

        btnSave.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(r -> {
                ProgressNote noteToUpdate = r.where(ProgressNote.class)
                        .equalTo("id", note.getId())
                        .findFirst();

                if (noteToUpdate != null) {
                    noteToUpdate.setDate(edtDate.getText().toString());
                    noteToUpdate.setTopic(edtTopic.getText().toString());
                    noteToUpdate.setWords(edtWords.getText().toString());
                    noteToUpdate.setSummary(edtSummary.getText().toString());
                    noteToUpdate.setConfidence(Integer.parseInt(edtConfidence.getText().toString()));
                    noteToUpdate.setDuration(edtDuration.getText().toString());
                    noteToUpdate.setNotes(edtNotes.getText().toString());
                }
            });
            realm.close();

            dialog.dismiss();
            if (onNoteUpdated != null) {
                onNoteUpdated.run();
            }
        });

        return dialog;
    }
}
