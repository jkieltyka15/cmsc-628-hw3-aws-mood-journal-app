package com.example.moodjournal.cognito;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.example.moodjournal.MainActivity;

public class CognitoConfirmSignUpHandler implements GenericHandler {

    private final Context context;
    private final MainActivity activity;


    /**
     * Constructs a CognitoConfirmSignUpHandler object
     *
     * @param context: Context of MainActivity
     */
    public CognitoConfirmSignUpHandler(Context context) {

        this.context = context;
        activity = (MainActivity) context;
    }


    /**
     * Called when confirming sign up is successful
     */
    @Override
    public void onSuccess() {
        activity.confirmSignUpSuccess();
    }


    /**
     * Called when confirming sign up fails
     *
     * @param exception: Exception that caused the failure
     */
    @Override
    public void onFailure(Exception exception) {
        activity.confirmSignUpFailed(exception.getMessage());
    }
}
