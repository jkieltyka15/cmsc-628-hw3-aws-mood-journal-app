package com.example.moodjournal.cognito;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.example.moodjournal.MainActivity;

public class CognitoForgotPasswordHandler implements ForgotPasswordHandler {

    private final MainActivity activity;
    private ForgotPasswordContinuation passwordContinuation;


    /**
     * Constructs a CognitoForgotPasswordHandler object
     *
     * @param context: Context of MainActivity
     */
    public CognitoForgotPasswordHandler(Context context) {
        activity = (MainActivity) context;
    }


    /**
     * Called when initiating password reset is successful
     */
    @Override
    public void onSuccess() {
        activity.passwordResetSuccess();
    }


    /**
     * Called when initiating password reset fails
     *
     * @param exception: Exception that caused the failure
     */
    @Override
    public void onFailure(Exception exception) {
        activity.passwordResetFailed(exception.getMessage());
    }


    /**
     * Sets the password continuation
     *
     * @param continuation REQUIRED: Continuation to the next step.
     */
    @Override
    public void getResetCode(ForgotPasswordContinuation continuation) {
        passwordContinuation = continuation;
    }


    /**
     * Gets the password continuation
     *
     * @return password continuation
     */
    public ForgotPasswordContinuation getPasswordContinuation() {
        return passwordContinuation;
    }
}
