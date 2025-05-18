package com.example.moodjournal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amazonaws.regions.Regions;
import com.example.moodjournal.cognito.CognitoHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private final LoginFragment loginFragment = new LoginFragment();
    private final ConfirmPasswordResetFragment passwordResetFragment = new ConfirmPasswordResetFragment();
    private final SignupFragment signupFragment = new SignupFragment();
    private final ConfirmSignupFragment confirmSignupFragment = new ConfirmSignupFragment();
    private final JournalFragment journalFragment = new JournalFragment();

    private CognitoHelper cognitoHelper;    // manages cognito session and functions
    private String currentUsername = "";    // username of current cognito user


    private class ChangeFragmentWork implements Runnable {

        Fragment fragment;  // fragment to populate fragment container


        /**
         * Constructs a new ChangeFragmentWork object
         *
         * @param fragment: Fragment to replace the current fragment that populates
         *                the fragment container
         */
        ChangeFragmentWork(Fragment fragment) {
            this.fragment = fragment;
        }


        /**
         * Replaces the current fragment that is being displayed and updates
         * the toolbar title
         */
        @Override
        public void run() {

            // display fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_main, fragment)
                    .commit();
        }
    }


    /**
     * Perform actions to attempt to sign up a Cognito user
     */
    private void cognitoSignUp() {

        // check that email is valid
        if (!signupFragment.isEmailValid()) {

            // set error text in signup fragment
            String errorText = getString(R.string.invalid_email_error);
            signupFragment.setErrorText(errorText);
            return;
        }

        // check that password is present
        if (signupFragment.getPassword().isEmpty()) {

            // set error text in signup fragment
            String errorText = getString(R.string.no_password_error);
            signupFragment.setErrorText(errorText);
            return;
        }

        // check that passwords match
        if (!signupFragment.isPasswordConfirmed()) {

            // set error text in signup fragment
            String errorText = getString(R.string.password_mismatch_error);
            signupFragment.setErrorText(errorText);
            return;
        }

        // get user credentials
        String username = signupFragment.getEmail();
        String email = signupFragment.getEmail();
        String password = signupFragment.getPassword();

        // attempt to initialize cognito user sign up
        cognitoHelper.signUp(username, password, email);
    }


    /**
     * Perform actions to attempt to confirm sign up for a Cognito user
     */
    private void cognitoConfirmSignUp() {

        // check that verification code is present
        if (confirmSignupFragment.getCode().isEmpty()) {

            // set error text in confirm fragment
            String errorText = getString(R.string.no_code_error);
            confirmSignupFragment.setErrorText(errorText);
            return;
        }

        // get verification credentials
        String code = confirmSignupFragment.getCode();

        // attempt to confirm Cognito user sign up
        cognitoHelper.ConfirmSignUp(currentUsername, code);
    }


    /**
     * Perform actions to attempt to login a Cognito user
     */
    private void cognitoLogin() {

        // check that email is valid
        if (!loginFragment.isEmailValid()) {

            // set error text in signup fragment
            String errorText = getString(R.string.invalid_email_error);
            loginFragment.setErrorText(errorText);
            return;
        }

        // check that password is present
        if (loginFragment.getPassword().isEmpty()) {

            // set error text in signup fragment
            String errorText = getString(R.string.no_password_error);
            loginFragment.setErrorText(errorText);
            return;
        }

        // get user credentials
        String username = loginFragment.getEmail();
        String password = loginFragment.getPassword();

        // attempt to sign in Cognito user
        cognitoHelper.signIn(username, password);
    }


    /**
     * Called when Activity is created
     *
     * @param savedInstanceState: Most recent data in the save instance state or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // perform standard setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup cognito helper
        cognitoHelper = new CognitoHelper(this, Regions.US_EAST_1);

        // display login fragment
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_main, loginFragment)
                    .commit();
        }
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

        // login button clicked in login fragment
        if (R.id.button_login == elementId) {
            cognitoLogin();
        }

        // reset password text clicked in login fragment
        else if (R.id.textView_reset_password == elementId) {
            // @todo implement password reset with AWS
        }

        // signup text clicked in login fragment
        else if (R.id.textView_signup == elementId) {

            // clear all text fields
            loginFragment.clearText();

            // navigate to signup fragment
            handler.post(new ChangeFragmentWork(signupFragment));
        }

        // signup button clicked in signup fragment
        else if (R.id.button_signup == elementId) {
            cognitoSignUp();
        }

        // login text clicked in signup fragment
        else if (R.id.textView_login == elementId) {

            // clear all text fields
            signupFragment.clearText();

            // navigate to login fragment
            handler.post(new ChangeFragmentWork(loginFragment));
        }

        // verify button clicked in confirm signup fragment
        else if (R.id.button_confirm_signup_verify == elementId) {
            cognitoConfirmSignUp();
        }

        // back text clicked in confirm signup fragment
        else if (R.id.textView_confirm_signup_back == elementId) {

            // clear current user session
            currentUsername = "";
            cognitoHelper.signOut();

            // clear all text fields
            confirmSignupFragment.clearText();

            // navigate to sign up fragment
            handler.post(new ChangeFragmentWork(signupFragment));
        }

        // login text clicked in signup fragment
        else if (R.id.toolbar_menu_sign_out == elementId) {

            // sign out current user
            cognitoHelper.signOut();

            // notify user is signed out
            final String signOutMsg = getString(R.string.cognito_signed_out);
            Toast.makeText(this, signOutMsg, Toast.LENGTH_SHORT).show();

            // navigate to login
            handler.post(new ChangeFragmentWork(loginFragment));
        }
    }


    /**
     * Actions to perform when Cognito sign in is successful
     */
    public void signInSuccess() {

        // save current username
        currentUsername = loginFragment.getEmail();

        // notify user is signed in
        final String signInMsg = getString(R.string.cognito_signed_in);
        Toast.makeText(this, signInMsg, Toast.LENGTH_SHORT).show();

        // clear all text field
        loginFragment.clearText();

        // navigate to journal fragment
        handler.post(new ChangeFragmentWork(journalFragment));
    }


    /**
     * Actions to perform when Cognito sign in fails
     *
     * @param message: Reason why sign in failed
     */
    public void signInFailed(String message) {
        currentUsername = "";
        loginFragment.setErrorText(message);
    }


    /**
     * Actions to perform when Cognito sign up is successful
     */
    public void signUpSuccess() {

        // save current username
        currentUsername = signupFragment.getEmail();

        // notify user account was created
        final String accountCreatedMsg = getString(R.string.cognito_account_created);
        Toast.makeText(this, accountCreatedMsg, Toast.LENGTH_SHORT).show();

        // clear all text fields
        signupFragment.clearText();

        // navigate to confirm sign up fragment
        handler.post(new ChangeFragmentWork(confirmSignupFragment));
    }


    /**
     * Actions to perform when Cognito sign up fails
     *
     * @param message: Reason why sign up failed
     */
    public void signUpFailed(String message) {
        currentUsername = "";
        signupFragment.setErrorText(message);
    }


    /**
     * Actions to perform when confirming sign up is successful
     */
    public void confirmSignUpSuccess() {

        // notify user account is verified
        final String accountVerifiedMsg = getString(R.string.cognito_account_verified);
        Toast.makeText(this, accountVerifiedMsg, Toast.LENGTH_SHORT).show();

        // clear all text fields
        confirmSignupFragment.clearText();

        // navigate to login fragment
        handler.post(new ChangeFragmentWork(loginFragment));
    }


    /**
     * Actions to perform when confirming sign up fails
     *
     * @param message: Reason why confirming sign up failed
     */
    public void confirmSignUpFailed(String message) {
        confirmSignupFragment.setErrorText(message);
    }


    /**
     * Actions to perform when password reset is successful
     */
    public void passwordResetSuccess() {

    }


    /**
     * Actions to perform when password reset fails
     *
     * @param message: Reason why password reset failed
     */
    public void passwordResetFailed(String message) {

    }
}
