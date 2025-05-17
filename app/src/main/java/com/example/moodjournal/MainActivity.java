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
    private final SignupFragment signupFragment = new SignupFragment();
    private final JournalFragment journalFragment = new JournalFragment();

    private CognitoHelper cognitoHelper;    // manages cognito session and functions


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
            // @todo Implement logging into AWS
            handler.post(new ChangeFragmentWork(journalFragment));
        }

        // reset password text clicked in login fragment
        else if (R.id.textView_reset_password == elementId) {
            // @todo implement password reset with AWS
        }

        // signup text clicked in login fragment
        else if (R.id.textView_signup == elementId) {
            handler.post(new ChangeFragmentWork(signupFragment));
        }

        // signup button clicked in signup fragment
        else if (R.id.button_signup == elementId) {
            // @todo Implement signing up with AWS
            handler.post(new ChangeFragmentWork(loginFragment));
        }

        // login text clicked in signup fragment
        else if (R.id.textView_login == elementId) {
            handler.post(new ChangeFragmentWork(loginFragment));
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

    }


    /**
     * Actions to perform when Cognito sign in fails
     *
     * @param message: Reason why sign in failed
     */
    public void signInFailed(String message) {

    }


    /**
     * Actions to perform when Cognito sign up is successful
     */
    public void signUpSuccess() {

    }


    /**
     * Actions to perform when Cognito sign up fails
     *
     * @param message: Reason why sign up failed
     */
    public void signUpFailed(String message) {

    }


    /**
     * Actions to perform when confirming sign up is successful
     */
    public void confirmSignUpSuccess() {

    }


    /**
     * Actions to perform when confirming sign up fails
     *
     * @param message: Reason why confirming sign up failed
     */
    public void confirmSignUpFailed(String message) {

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
