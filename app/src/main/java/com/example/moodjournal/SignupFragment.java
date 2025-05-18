package com.example.moodjournal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class SignupFragment extends Fragment implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private View.OnClickListener callback;  // callback to view when ui element is clicked

    private EditText userEmailEditText;
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


    private class ClearText implements Runnable {

        /**
         * Clears all editable text
         */
        @Override
        public void run() {
            userEmailEditText.setText("");
            userPasswordEditText.setText("");
            userPasswordConfirmEditText.setText("");
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
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);

        // initialize error TextView
        errorTextView = view.findViewById(R.id.textView_signup_error);

        // initialize edit text elements
        userEmailEditText = view.findViewById(R.id.editText_signup_email);
        userPasswordEditText = view.findViewById(R.id.editText_signup_password);
        userPasswordConfirmEditText = view.findViewById(R.id.editText_signup_password_confirm);

        // initialize clickable elements
        Button signupButton = view.findViewById(R.id.button_signup);
        TextView loginTextView = view.findViewById(R.id.textView_login);

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
     * Checks if the email in the EditText is valid
     *
     * @return True if email is valid. Otherwise false
     */
    public boolean isEmailValid() {

        String email = userEmailEditText.getText().toString();
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
     * Gets value held in email EditText as a String
     *
     * @return Value of email EditText
     */
    public String getEmail() {
        return userEmailEditText.getText().toString();
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
        handler.post(new SignupFragment.SetErrorText(text));
    }


    /**
     * Clears all editable text
     */
    public void clearText() {
        handler.post(new SignupFragment.ClearText());
    }
}