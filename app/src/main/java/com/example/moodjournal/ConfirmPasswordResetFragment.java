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


public class ConfirmPasswordResetFragment extends Fragment implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private View.OnClickListener callback;  // callback to view when ui element is clicked

    private EditText codeEditText;
    private EditText userPasswordEditText;
    private EditText userPasswordConfirmEditText;
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
        View view =  inflater.inflate(R.layout.fragment_confirm_password_reset, container, false);

        // initialize error TextView
        errorTextView = view.findViewById(R.id.textView_confirm_password_reset_error);

        // initialize edit text elements
        codeEditText = view.findViewById(R.id.editText_confirm_password_reset_code);
        userPasswordEditText = view.findViewById(R.id.editText_confirm_password_reset_password);
        userPasswordConfirmEditText = view.findViewById(R.id.editText_confirm_password_reset_password_confirm);

        // initialize clickable elements
        Button signupButton = view.findViewById(R.id.button_confirm_password_reset_reset_password);
        TextView loginTextView = view.findViewById(R.id.textView_confirm_password_reset_back);

        // setup clickable element listeners
        signupButton.setOnClickListener(this);
        loginTextView.setOnClickListener(this);

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

            // signup button clicked
            if (R.id.button_signup == elementId) {

                // clear error text
                this.setErrorText("");

                // perform callback
                callback.onClick(view);
            }

            // login text clicked
            else if (R.id.textView_login == elementId) {

                // clear error text
                this.setErrorText("");

                // perform callback
                callback.onClick(view);
            }
        }
    }


    /**
     * Checks if the password and confirm password EditTexts match
     *
     * @return True if passwords match. Otherwise false
     */
    public boolean isPasswordConfirmed() {

        final String password = userPasswordEditText.getText().toString();
        final String passwordConfirm = userPasswordConfirmEditText.getText().toString();

        return password.equals(passwordConfirm);
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
     * Gets value held in password EditText as a String
     *
     * @return Value of password EditText
     */
    public String getPassword() {
        return userPasswordEditText.getText().toString();
    }


    /**
     * Sets text in error TextView
     *
     * @param text: Text to display in error TextView
     */
    public void setErrorText(String text) {
        handler.post(new ConfirmPasswordResetFragment.SetErrorText(text));
    }


    /**
     * Clears all editable text
     */
    public void clearText() {
        codeEditText.setText("");
        userPasswordEditText.setText("");
        userPasswordConfirmEditText.setText("");
        setErrorText("");
    }
}