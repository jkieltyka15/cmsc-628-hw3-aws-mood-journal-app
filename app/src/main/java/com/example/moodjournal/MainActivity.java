package com.example.moodjournal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amazonaws.regions.Regions;
import com.example.moodjournal.cognito.CognitoHelper;
import com.example.moodjournal.lambda.JournalEntry;
import com.example.moodjournal.lambda.JournalErrorEntry;
import com.example.moodjournal.lambda.LambdaGetCallback;
import com.example.moodjournal.lambda.LambdaPostCallback;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private final LoginFragment loginFragment = new LoginFragment();
    private final PasswordResetFragment passwordResetFragment = new PasswordResetFragment();
    private final ConfirmPasswordResetFragment confirmPasswordResetFragment = new ConfirmPasswordResetFragment();
    private final SignupFragment signupFragment = new SignupFragment();
    private final ConfirmSignupFragment confirmSignupFragment = new ConfirmSignupFragment();
    private final JournalFragment journalFragment = new JournalFragment();

    private CognitoHelper cognitoHelper; // manages cognito session and functions
    private String currentUsername = ""; // username of current cognito user
    private String currentUserAccessToken = ""; // used for authorization
    private String currentUserIdToken = ""; // used for identification
    private String currentUserRefreshToken = ""; // used for re-authentication


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


    private class MakeShortToastWork implements Runnable {

        String message;

        /**
         * Constructs a new MakeShortToastWork object
         *
         * @param message: Message for toast
         */
        MakeShortToastWork(String message) {
            this.message = message;
        }


        /**
         * Creates a short toast message
         */
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
     * Perform actions to attempt initialize a Cognito user password reset
     */
    private void cognitoResetPassword() {

        // check that email is valid
        if (!passwordResetFragment.isEmailValid()) {

            // set error text in signup fragment
            String errorText = getString(R.string.invalid_email_error);
            passwordResetFragment.setErrorText(errorText);
            return;
        }

        // get username
        String username = passwordResetFragment.getEmail();

        // attempt to initialize cognito user password reset
        cognitoHelper.initiatePasswordReset(username);
    }


    /**
     * Perform actions to reset a Cognito user password
     */
    private void cognitoConfirmResetPassword() {

        // check that verification code is present
        if (confirmPasswordResetFragment.getCode().isEmpty()) {

            // set error text in confirm fragment
            String errorText = getString(R.string.no_code_error);
            confirmPasswordResetFragment.setErrorText(errorText);
            return;
        }

        // check that password is present
        if (confirmPasswordResetFragment.getPassword().isEmpty()) {

            // set error text in signup fragment
            String errorText = getString(R.string.no_password_error);
            confirmPasswordResetFragment.setErrorText(errorText);
            return;
        }

        // check that passwords match
        if (!confirmPasswordResetFragment.isPasswordConfirmed()) {

            // set error text in signup fragment
            String errorText = getString(R.string.password_mismatch_error);
            confirmPasswordResetFragment.setErrorText(errorText);
            return;
        }

        // get verification credentials
        String code = confirmPasswordResetFragment.getCode();
        String password = confirmPasswordResetFragment.getPassword();

        // attempt to reset user password
        cognitoHelper.confirmPasswordReset(code, password);
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
     * Perform actions to attempt to submit a journal entry via lambda
     */
    private void lambdaSubmitEntry() {

        // get entry fragment
        EntryFragment entryFragment = journalFragment.getEntryFragment();

        // get data from entry fragment
        String mood = entryFragment.getMood();
        String notes = entryFragment.getNotes();

        // initialize http client
        OkHttpClient client = new OkHttpClient();

        // create json payload
        Map<String, String> payload = new HashMap<>();

        // add mood to the payload
        payload.put("mood", mood);

        // add notes to the payload if there are any
        if (!notes.isEmpty()) {
            payload.put("notes", notes);
        }

        // initialize json object
        Gson gson = new Gson();
        String jsonBody = gson.toJson(payload);

        // create the body
        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json; charset=utf-8")
        );

        // get POST url
        String url = getString(R.string.lambda_post_entry_url);

        // build the POST request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + currentUserAccessToken)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // perform POST request
        client.newCall(request).enqueue(new LambdaPostCallback(this));
    }


    /**
     * Perform actions to attempt to get journal entries via lambda
     */
    private void lambdaGetEntries() {

        // initialize http client
        OkHttpClient client = new OkHttpClient();

        // get GET url
        String url = getString(R.string.lambda_get_entries_url);

        // build the GET request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + currentUserAccessToken)
                .addHeader("Content-Type", "application/json")
                .get()
                .build();

        // clear old entries
        journalFragment.getHistoryFragment().updateJournalEntries(Collections.emptyList());

        // perform GET request
        client.newCall(request).enqueue(new LambdaGetCallback(this));
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

            // clear all text fields
            loginFragment.clearText();

            // navigate to signup fragment
            handler.post(new ChangeFragmentWork(passwordResetFragment));
        }

        // signup text clicked in login fragment
        else if (R.id.textView_signup == elementId) {

            // clear all text fields
            loginFragment.clearText();

            // navigate to signup fragment
            handler.post(new ChangeFragmentWork(signupFragment));
        }

        // request password reset button clicked in reset password fragment
        else if (R.id.button_password_reset_request_password_reset == elementId) {

            // request password reset
            cognitoResetPassword();

            // notify user request was sent
            final String passwordResetMsg = getString(R.string.cognito_password_reset_request_sent);
            handler.post(new MakeShortToastWork(passwordResetMsg));

            // clear all text fields
            passwordResetFragment.clearText();

            // navigate to confirm password reset fragment
            handler.post(new ChangeFragmentWork(confirmPasswordResetFragment));
        }

        // back text clicked in reset password fragment
        else if (R.id.textView_password_reset_login == elementId) {

            // clear all text fields
            passwordResetFragment.clearText();

            // navigate to login fragment
            handler.post(new ChangeFragmentWork(loginFragment));
        }

        // reset password button clicked in confirm password reset fragment
        if (R.id.button_confirm_password_reset_reset_password == elementId) {
            cognitoConfirmResetPassword();
        }

        // back text clicked in confirm password reset fragment
        else if (R.id.textView_confirm_password_reset_back == elementId) {

            // clear all text fields
            confirmPasswordResetFragment.clearText();

            // navigate to login fragment
            handler.post(new ChangeFragmentWork(passwordResetFragment));
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

        // sign out clicked in toolbar menu
        else if (R.id.toolbar_menu_sign_out == elementId) {

            // sign out current user
            cognitoHelper.signOut();

            // notify user is signed out
            final String signOutMsg = getString(R.string.cognito_signed_out);
            handler.post(new MakeShortToastWork(signOutMsg));

            // clear mood journal
            journalFragment.getEntryFragment().clearEntry();

            // navigate to login
            handler.post(new ChangeFragmentWork(loginFragment));
        }

        // history clicked in toolbar menu
        else if (R.id.toolbar_menu_history == elementId) {
            lambdaGetEntries();
        }

        // submit button clicked in entry fragment
        else if (R.id.button_entry_submit == elementId) {
            lambdaSubmitEntry();
        }
    }


    /**
     * Actions to perform when Cognito sign in is successful
     */
    public void signInSuccess(String accessToken, String idToken, String refreshToken) {

        // clear previous user
        cognitoHelper.signOut();

        // save current user credentials
        currentUsername = loginFragment.getEmail();
        currentUserAccessToken = accessToken;
        currentUserIdToken = idToken;
        currentUserRefreshToken = refreshToken;

        // notify user is signed in
        final String signInMsg = getString(R.string.cognito_signed_in);
        handler.post(new MakeShortToastWork(signInMsg));

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
        handler.post(new MakeShortToastWork(accountCreatedMsg));

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
        handler.post(new MakeShortToastWork(accountVerifiedMsg));

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

        // notify user password was reset
        final String passwordResetMsg = getString(R.string.cognito_password_reset);
        handler.post(new MakeShortToastWork(passwordResetMsg));

        // clear all text fields
        confirmPasswordResetFragment.clearText();

        // navigate to login fragment
        handler.post(new ChangeFragmentWork(loginFragment));
    }


    /**
     * Actions to perform when password reset fails
     *
     * @param message: Reason why password reset failed
     */
    public void passwordResetFailed(String message) {
        confirmPasswordResetFragment.setErrorText(message);
    }


    /**
     * Actions to perform when posting a journal entry is successful
     */
    public void journalEntryPostSuccess() {

        // notify user entry was saved
        final String savedMsg = getString(R.string.lambda_entry_submitted);
        handler.post(new MakeShortToastWork(savedMsg));

        // get entry fragment
        EntryFragment entryFragment = journalFragment.getEntryFragment();

        // clear entry fragment
        entryFragment.clearEntry();
    }


    /**
     * Actions to perform when posting a journal entry fails
     *
     * @param message: Reason why posting journal entry failed
     */
    public void journalEntryPostFailed(String message) {
        journalFragment.getEntryFragment().setErrorText(message);
    }


    /**
     * Actions to perform when getting journal entries is successful
     *
     * @param entries: List of journal entries
     */
    public void journalEntriesGetSuccess(List<JournalEntry> entries) {
        journalFragment.getHistoryFragment().updateJournalEntries(entries);
    }


    /**
     * Actions to perform when posting a journal entry fails
     *
     * @param message: Reason why posting journal entry failed
     */
    public void journalEntriesGetFailed(String message) {

        List<JournalEntry> errorList = new ArrayList<>();
        errorList.add(new JournalErrorEntry(message));
        journalFragment.getHistoryFragment().updateJournalEntries(errorList);
    }
}
