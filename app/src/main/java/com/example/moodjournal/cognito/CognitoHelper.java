package com.example.moodjournal.cognito;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.regions.Regions;
import com.example.moodjournal.R;


public class CognitoHelper {

    private final Context context;
    private final CognitoUserPool userPool;
    private final CognitoConfirmSignUpHandler confirmSignUpHandler;
    private final CognitoForgotPasswordHandler forgotPasswordHandler;
    private final CognitoSignUpHandler signUpHandler;


    /**
     * Constructs a CognitoHelper object
     *
     * @param context: Context of MainActivity
     * @param region: AWS region of cognito user pool
     */
    public CognitoHelper(Context context, Regions region) {

        this.context = context;

        final String userPoolId = context.getString(R.string.cognito_user_pool_id);
        final String clientId = context.getString(R.string.cognito_client_id);

        // setup  user pool
        userPool = new CognitoUserPool(context, userPoolId, clientId, null, region);

        // setup handlers
        confirmSignUpHandler = new CognitoConfirmSignUpHandler(context);
        forgotPasswordHandler = new CognitoForgotPasswordHandler(context);
        signUpHandler = new CognitoSignUpHandler(context);
    }


    /**
     * Attempt to sign up for a Cognito account
     *
     * @param username: Username of user
     * @param password: Password for user
     * @param email: Email of user
     */
    public void signUp(String username, String password, String email) {

        // setup user attributes
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute("email", email);

        // attempt to signup in the background
        userPool.signUpInBackground(username, password, userAttributes, null, signUpHandler);
    }


    /**
     * Confirms sign up for a Cognito account
     *
     * @param username: Username of user
     * @param code: Code sent to user to confirm sign up
     */
    public void ConfirmSignUp(String username, String code) {

        // get cognito user
        CognitoUser user = userPool.getUser(username);

        // attempt to confirm signup in the background
        user.confirmSignUpInBackground(code, false, confirmSignUpHandler);
    }


    /**
     * Attempt to sign in using Cognito
     *
     * @param username: Username of user
     * @param password: Password of user
     */
    public void signIn(String username, String password) {

        // get cognito user
        CognitoUser user = userPool.getUser(username);

        // create authentication handler
        CognitoAuthenticationHandler handler = new CognitoAuthenticationHandler(username, password, context);

        // attempt to sign in
        user.getSessionInBackground(handler);
    }


    /**
     * Sign out current Cognito user
     */
    public void signOut() {
        userPool.getCurrentUser().signOut();
    }


    /**
     * Start a password reset for a Cognito user
     *
     * @param username: Username of user
     */
    public void initiatePasswordReset(String username) {

        // get user from user pool
        CognitoUser user = userPool.getUser(username);

        // initialize a password reset
        user.forgotPasswordInBackground(forgotPasswordHandler);
    }


    /**
     * Confirms password reset for Cognito user
     *
     * @param code: Verification code sent to user
     * @param newPassword: New password for user
     */
    public void confirmPasswordReset(String code, String newPassword) {

        // get password continuation
        ForgotPasswordContinuation passwordContinuation = forgotPasswordHandler.getPasswordContinuation();

        if (null == passwordContinuation) {
            return;
        }

        // attempt to reset password
        passwordContinuation.setVerificationCode(code);
        passwordContinuation.setPassword(newPassword);
        passwordContinuation.continueTask();
    }
}
