package com.example.moodjournal.cognito;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.example.moodjournal.MainActivity;

public class CognitoSignUpHandler implements SignUpHandler {

    private final MainActivity activity;


    /**
     * Constructs a CognitoSignUpHandler object
     *
     * @param context: Context of MainActivity
     */
    public CognitoSignUpHandler(Context context) {
        activity = (MainActivity) context;
    }


    /**
     * Called when sign up is successful
     *
     * @param user: Cognito user that was successfully signed up
     * @param signUpResult: Result of a sign up action
     */
    @Override
    public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
        activity.signUpSuccess();
    }


    /**
     * Called when sign up fails
     *
     * @param exception: Exception that caused the failure
     */
    @Override
    public void onFailure(Exception exception) {
        activity.signUpFailed(exception.getMessage());
    }
}
