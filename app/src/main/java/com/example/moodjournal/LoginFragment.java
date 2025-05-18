package com.example.moodjournal;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private View.OnClickListener callback;  // callback to view when ui element is clicked

    private EditText userEmailEditText;
    private EditText userPasswordEditText;
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // initialize error TextView
        errorTextView = view.findViewById(R.id.textView_login_error);

        // initialize edit text elements
        userEmailEditText = view.findViewById(R.id.editText_login_email);
        userPasswordEditText = view.findViewById(R.id.editText_login_password);

        // initialize clickable elements
        Button loginButton = view.findViewById(R.id.button_login);
        TextView resetPasswordTextView = view.findViewById(R.id.textView_reset_password);
        TextView signupTextView = view.findViewById(R.id.textView_signup);

        // setup clickable element listeners
        loginButton.setOnClickListener(this);
        resetPasswordTextView.setOnClickListener(this);
        signupTextView.setOnClickListener(this);

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

            // login button clicked
            if (R.id.button_login == elementId) {

                // clear error text
                this.setErrorText("");

                // perform callback
                callback.onClick(view);
            }

            // reset password text clicked
            else if (R.id.textView_reset_password == elementId) {

                // clear error text
                this.setErrorText("");

                // perform callback
                callback.onClick(view);
            }

            // signup text clicked
            else if (R.id.textView_signup == elementId) {

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
        handler.post(new LoginFragment.SetErrorText(text));
    }


    /**
     * Clears all editable text
     */
    public void clearText() {
        userEmailEditText.setText("");
        userPasswordEditText.setText("");
        setErrorText("");
    }

}