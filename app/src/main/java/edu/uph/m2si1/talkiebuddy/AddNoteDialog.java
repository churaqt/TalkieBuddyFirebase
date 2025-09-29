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

public class AddNoteDialog extends DialogFragment {

    public interface NoteSavedListener {
        void onNoteSaved();
    }

    private NoteSavedListener listener;

    public void setNoteSavedListener(NoteSavedListener listener) {
        this.listener = listener;
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

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();

        btnSave.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(r -> {
                Number maxId = r.where(ProgressNote.class).max("id");
                int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;

                ProgressNote note = r.createObject(ProgressNote.class, nextId);
                note.setDate(edtDate.getText().toString());
                note.setTopic(edtTopic.getText().toString());
                note.setWords(edtWords.getText().toString());
                note.setSummary(edtSummary.getText().toString());
                note.setConfidence(Integer.parseInt(edtConfidence.getText().toString()));
                note.setDuration(edtDuration.getText().toString());
                note.setNotes(edtNotes.getText().toString());
            });
            realm.close();
            dialog.dismiss();

            if (listener != null) {
                listener.onNoteSaved();
            }
        });

        return dialog;
    }
}
