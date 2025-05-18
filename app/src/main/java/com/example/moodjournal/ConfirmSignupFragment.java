package com.example.moodjournal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class ConfirmSignupFragment extends Fragment implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private View.OnClickListener callback;  // callback to view when ui element is clicked

    private EditText codeEditText;
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


    private class ClearText implements Runnable {

        /**
         * Clears all editable text
         */
        @Override
        public void run() {
            codeEditText.setText("");
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
        View view = inflater.inflate(R.layout.fragment_confirm_signup, container, false);

        // initialize error TextView
        errorTextView = view.findViewById(R.id.textView_confirm_signup_error);

        // initialize edit text elements
        codeEditText = view.findViewById(R.id.editText_confirm_signup_code);

        // initialize clickable elements
        Button verifyButton = view.findViewById(R.id.button_confirm_signup_verify);
        TextView backTextView = view.findViewById(R.id.textView_confirm_signup_back);

        // setup clickable element listeners
        verifyButton.setOnClickListener(this);
        backTextView.setOnClickListener(this);

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

            // verify button clicked
            if (R.id.button_confirm_signup_verify == elementId) {

                // clear error text
                this.setErrorText("");

                // perform callback
                callback.onClick(view);
            }

            // back text clicked
            else if (R.id.textView_confirm_signup_back == elementId) {

                // clear error text
                this.setErrorText("");

                // perform callback
                callback.onClick(view);
            }
        }
    }


    /**
     * Gets value held in verification code EditText as a String
     *
     * @return Value of verification code EditText
     */
    public String getCode() {
        return codeEditText.getText().toString();
    }


    /**
     * Sets text in error TextView
     *
     * @param text: Text to display in error TextView
     */
    public void setErrorText(String text) {
        handler.post(new ConfirmSignupFragment.SetErrorText(text));
    }


    /**
     * Clears all editable text
     */
    public void clearText() {
        handler.post(new ConfirmSignupFragment.ClearText());
    }
}