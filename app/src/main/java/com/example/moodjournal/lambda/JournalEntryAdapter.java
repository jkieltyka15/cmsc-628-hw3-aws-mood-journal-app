package com.example.moodjournal.lambda;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodjournal.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.EntryViewHolder> {

    private static final int TYPE_ENTRY = 0;
    private static final int TYPE_ERROR = 1;

    private final List<JournalEntry> entries;


    public static class EntryViewHolder extends RecyclerView.ViewHolder {

        TextView moodText, notesText, timestampText;


        /**
         * Constructs a EntryViewHolder object
         *
         * @param itemView: View of item
         */
        public EntryViewHolder(@NonNull View itemView) {

            super(itemView);
            moodText = itemView.findViewById(R.id.textView_mood);
            notesText = itemView.findViewById(R.id.textView_notes);
            timestampText = itemView.findViewById(R.id.textView_timestamp);
        }
    }


    public static class ErrorViewHolder extends EntryViewHolder {

        TextView errorText;


        /**
         * Constructs a ErrorViewHolder object
         *
         * @param itemView: View of item
         */
        public ErrorViewHolder(@NonNull View itemView) {

            super(itemView);
            errorText = itemView.findViewById(R.id.textView_error);
        }
    }


    /**
     * Constructs a JournalEntryAdapter object
     *
     * @param entries: List of journal entries
     */
    public JournalEntryAdapter(List<JournalEntry> entries) {
        this.entries = entries;
    }


    /**
     * Called upon creation of ViewHolder
     *
     * @param parent: ViewGroup into which the new View will be added after it is bound to
     *               an adapter position
     * @param viewType: View type of the new View
     *
     * @return New EntryViewHolder based on view
     */
    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // view is error entry
        if (viewType == TYPE_ERROR) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_journal_error, parent, false);

            return new ErrorViewHolder(view);

        }

        // view is journal entry
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_journal_entry, parent, false);

            return new EntryViewHolder(view);
    }


    /**
     * Called when ViewHolder is bound
     *
     * @param holder: ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set
     * @param position: Position of item within adapter data set.
     */
    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {

        // get entry at current position
        JournalEntry entry = entries.get(position);

        // view is error entry
        if (holder instanceof ErrorViewHolder) {
            ((ErrorViewHolder) holder).errorText.setText(((JournalErrorEntry) entry).getErrorMessage());
            return;
        }

        // set text for mood
        String mood = holder.itemView.getContext().getString(R.string.journal_entry_mood_label);
        mood += " " + entry.getMood();
        holder.moodText.setText(mood);

        // set text for notes
        String notes = holder.itemView.getContext().getString(R.string.journal_entry_notes_label);
        notes += " " + entry.getNotes();
        holder.notesText.setText(notes);

        // format timestamp to readable format
        String formattedDate = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault())
                .format(new Date(entry.getTimestamp() * 1000));

        // set text for formatted timestamp
        String timestamp = holder.itemView.getContext().getString(R.string.journal_entry_timestamp_label);
        timestamp += " " + formattedDate;
        holder.timestampText.setText(timestamp);
    }


    /**
     * Gets the number of entries
     *
     * @return Number of entries
     */
    @Override
    public int getItemCount() {
        return entries.size();
    }


    /**
     * Gets the view type
     *
     * @param position: Position to query
     *
     * @return Which type the view is
     */
    @Override
    public int getItemViewType(int position) {
        return (entries.get(position) instanceof JournalErrorEntry) ? TYPE_ERROR : TYPE_ENTRY;
    }
}
