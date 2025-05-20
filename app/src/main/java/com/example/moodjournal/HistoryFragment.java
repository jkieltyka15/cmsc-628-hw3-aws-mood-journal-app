package com.example.moodjournal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moodjournal.lambda.JournalEntry;
import com.example.moodjournal.lambda.JournalEntryAdapter;

import java.util.List;


public class HistoryFragment extends Fragment {

    private final Handler handler = new Handler();  // handles UI changes safely

    private RecyclerView historyRecyclerView;
    private TextView emptyMessageText;


    private class UpdateJournalEntriesWork implements Runnable {

        List<JournalEntry> entries;


        /**
         * Constructs a new ChangeFragmentWork object
         *
         * @param entries: New entries to display
         */
        UpdateJournalEntriesWork(List<JournalEntry> entries) {
            this.entries = entries;
        }


        /**
         * Updates the journal entries being displayed
         */
        @Override
        public void run() {

            if (null == historyRecyclerView || null == emptyMessageText) {
                return;
            }

            // no entries to display
            if (entries.isEmpty()) {

                historyRecyclerView.setVisibility(View.GONE);
                emptyMessageText.setVisibility(View.VISIBLE);
                return;
            }

            // display journal entries
            historyRecyclerView.setVisibility(View.VISIBLE);
            emptyMessageText.setVisibility(View.GONE);
            JournalEntryAdapter adapter = new JournalEntryAdapter(entries);
            historyRecyclerView.setAdapter(adapter);
        }
    }

    /**
     * Called when fragment view is created
     *
     * @param inflater: Used to inflate any views in the fragment
     * @param container: Parent view that the fragment UI should be attached to
     * @param savedInstanceState: Previous saved state
     *
     * @return View of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // initialize recycle view
        historyRecyclerView = view.findViewById(R.id.recycle_view_history);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // initialize text view
        emptyMessageText = view.findViewById(R.id.text_empty_message);

        return view;
    }

    /**
     * Updates the journal entries being displayed
     *
     * @param entries: New entries to display
     */
    public void updateJournalEntries(List<JournalEntry> entries) {
        handler.post(new HistoryFragment.UpdateJournalEntriesWork(entries));
    }
}