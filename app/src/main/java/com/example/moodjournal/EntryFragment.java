package com.example.moodjournal;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EntryFragment extends Fragment implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private View.OnClickListener callback;  // callback to view when ui element is clicked

    private TextView dateTextView;
    private Spinner moodSpinner;
    private EditText notesTextView;
    private TextView errorTextView;


    private class SetErrorText implements Runnable {

        String text;  // text to display in error TextView


        /**
         * Constructs a new SetErrorText object
         *
         * @param text: Text to display in error TextView
         */
        SetErrorText(String text) {
            this.text = text;
        }


        /**
         * Sets text in error TextView
         */
        @Override
        public void run() {
            errorTextView.setText(text);
        }
    }


    private class SetDateText implements Runnable {

        /**
         * Sets text in date TextView to current date
         */
        @Override
        public void run() {

            // get date and format
            SimpleDateFormat date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String dateText = date.format(new Date());

            // set text
            dateTextView.setText(dateText);
        }
    }


    private class ClearEntry implements Runnable {

        /**
         * Clears the entry
         */
        @Override
        public void run() {
            dateTextView.setText("");
            notesTextView.setText("");
            errorTextView.setText("");
        }
    }


    /**
     * Called when fragment is attached
     *
     * @param context: Context
     */
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        // setup callback
        if (context instanceof View.OnClickListener) {
            callback = (View.OnClickListener) context;
        }

        // context must implement OnClickListener
        else {
            throw new ClassCastException(context + " must implement OnClickListener");
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
        View view = inflater.inflate(R.layout.fragment_entry, container, false);

        // initialize error TextView
        errorTextView = view.findViewById(R.id.textView_entry_error);

        // initialize text elements
        dateTextView = view.findViewById(R.id.textView_entry_date);
        notesTextView = view.findViewById(R.id.multilineEditText_entry_notes);

        // set date
        handler.post(new EntryFragment.SetDateText());

        // initialize spinners
        moodSpinner = view.findViewById(R.id.spinner_entry_mood);

        // load mood items
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                view.getContext(),
                R.array.entry_moods,
                R.layout.spinner_mood_selected
        );

        // set mood spinner adapter
        adapter.setDropDownViewResource(R.layout.spinner_moods);
        moodSpinner.setAdapter(adapter);

        // initialize clickable elements
        Button submitButton = view.findViewById(R.id.button_entry_submit);

        // setup clickable element listeners
        submitButton.setOnClickListener(this);

        // inflate the layout for this fragment
        return view;
    }


    /**
     * Called when an UI element is clicked
     *
     * @param view: View that was clicked
     */
    @Override
    public void onClick(View view) {

        // get ID of element clicked
        int elementId = view.getId();

        // perform callback
        if (null != callback) {

            // submit button clicked
            if (R.id.button_entry_submit == elementId) {

                // clear error text
                this.setErrorText("");

                // perform callback
                callback.onClick(view);
            }
        }
    }

    /**
     * Sets text in error TextView
     *
     * @param text: Text to display in error TextView
     */
    public void setErrorText(String text) {
        handler.post(new EntryFragment.SetErrorText(text));
    }


    /**
     * Clears the entry
     */
    public void clearEntry() {
        handler.post(new EntryFragment.ClearEntry());
    }
}