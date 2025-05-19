package com.example.moodjournal.lambda;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.moodjournal.MainActivity;

import java.io.IOException;


public class LambdaPostCallback implements Callback {

    private final MainActivity activity;


    /**
     * Constructs a LambdaPostCallback object
     *
     * @param context: Context of MainActivity
     */
    public LambdaPostCallback(Context context) {
        activity = (MainActivity) context;
    }


    /**
     * Called when the request fails
     *
     * @param call: Call that was executed
     * @param e: Exception that caused failure
     */
    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        activity.journalEntryPostFailed(e.getMessage());
    }


    /**
     * Called when a response is received from the request
     *
     * @param call: Call that was executed
     * @param response: Response from request
     * @throws IOException: Possible exception
     */
    @Override
    public void onResponse(@NonNull Call call, Response response) throws IOException {

        // entry added
        if (response.isSuccessful()) {
            activity.journalEntryPostSuccess();
        }

        // entry not added
        else {
            String msg = "Error: " + response.code();

            // add details if available
            if (null != response.body()) {
                msg += " - " + response.body().string();
            }

            activity.journalEntryPostFailed(msg);
        }
    }
}
