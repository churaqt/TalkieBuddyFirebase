package edu.uph.m2si1.talkiebuddy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uph.m2si1.talkiebuddy.R;
import edu.uph.m2si1.talkiebuddy.model.ProgressNote;
import io.realm.Realm;

public class ProgressNoteAdapter extends RecyclerView.Adapter<ProgressNoteAdapter.ViewHolder> {

    public interface OnItemActionListener {
        void onEdit(ProgressNote note);
    }

    private final Context context;
    private final List<ProgressNote> progressNoteList;
    private final OnItemActionListener listener;

    public ProgressNoteAdapter(Context context, List<ProgressNote> progressNoteList, OnItemActionListener listener) {
        this.context = context;
        this.progressNoteList = progressNoteList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txvDate, txvTopic, txvWords, txvSummary, txvConfidence, txvDuration, txvNotes;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txvDate = itemView.findViewById(R.id.txvDate);
            txvTopic = itemView.findViewById(R.id.txvTopic);
            txvWords = itemView.findViewById(R.id.txvWords);
            txvSummary = itemView.findViewById(R.id.txvSummary);
            txvConfidence = itemView.findViewById(R.id.txvConfidence);
            txvDuration = itemView.findViewById(R.id.txvDuration);
            txvNotes = itemView.findViewById(R.id.txvNotes);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ProgressNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_progress_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressNoteAdapter.ViewHolder holder, int position) {
        ProgressNote note = progressNoteList.get(position);

        holder.txvDate.setText("Date: " + note.getDate());
        holder.txvTopic.setText("Topic: " + note.getTopic());
        holder.txvWords.setText("Words Learned: " + note.getWords());
        holder.txvSummary.setText("Summary: " + note.getSummary());
        holder.txvConfidence.setText("Confidence: " + note.getConfidence());
        holder.txvDuration.setText("Duration: " + note.getDuration());
        holder.txvNotes.setText("Notes: " + note.getNotes());


        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                ProgressNote unmanagedNote = new ProgressNote();
                unmanagedNote.setId(note.getId());
                unmanagedNote.setDate(note.getDate());
                unmanagedNote.setTopic(note.getTopic());
                unmanagedNote.setWords(note.getWords());
                unmanagedNote.setSummary(note.getSummary());
                unmanagedNote.setConfidence(note.getConfidence());
                unmanagedNote.setDuration(note.getDuration());
                unmanagedNote.setNotes(note.getNotes());

                listener.onEdit(unmanagedNote);
            }
        });


        holder.btnDelete.setOnClickListener(v -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(r -> {
                ProgressNote deleteNote = r.where(ProgressNote.class).equalTo("id", note.getId()).findFirst();
                if (deleteNote != null) {
                    deleteNote.deleteFromRealm();
                    progressNoteList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, progressNoteList.size());
                }
            });
            realm.close();
        });
    }

    @Override
    public int getItemCount() {
        return progressNoteList.size();
    }
}
