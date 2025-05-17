package com.example.moodjournal.cognito;

import android.content.Context;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.example.moodjournal.MainActivity;


public class CognitoAuthenticationHandler implements AuthenticationHandler {

    private final String username;
    private final String password;
    private final Context context;
    private final MainActivity activity;


    /**
     * Constructs a CognitoAuthenticationHandler object
     *
     * @param username: Username of user being authenticated
     * @param password: Password of user
     * @param context: Context of MainActivity
     */
    public CognitoAuthenticationHandler(String username, String password, Context context) {

        this.username = username;
        this.password = password;
        this.context = context;
        activity = (MainActivity) context;
    }


    /**
     * Called when sign ip is successful
     *
     * @param userSession: Current Cognito user session
     * @param newDevice: New Cognito device
     */
    @Override
    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
        activity.signInSuccess();
    }


    /**
     * Called when sign in fails
     *
     * @param exception: Exception that caused the failure
     */
    @Override
    public void onFailure(Exception exception) {
        activity.signInFailed(exception.getMessage());
    }


    /**
     * Gets authentication details and allows them to persist when they are available
     *
     * @param authenticationContinuation: Used to continue with the authentication process when
     *                                    the user authentication details are available.
     * @param userId: User ID for authentication. Null if ID is not available
     */
    @Override
    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {

        AuthenticationDetails authDetails = new AuthenticationDetails(username, password, null);
        authenticationContinuation.setAuthenticationDetails(authDetails);
        authenticationContinuation.continueTask();
    }


    /**
     * Gets MFA code (not implemented)
     *
     * @param continuation: Medium through which the MFA will be delivered
     */
    @Override
    public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
        // not implemented
    }


    /**
     * Creates an authentication challenge (not implemented)
     *
     * @param continuation: contains details about the challenge and allows responds to the
     *                      challenge
     */
    @Override
    public void authenticationChallenge(ChallengeContinuation continuation) {
        // not implemented
    }
}
