package com.example.moodjournal.lambda;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.moodjournal.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LambdaGetCallback implements Callback {

    private final MainActivity activity;


    /**
     * Constructs a LambdaGetCallback object
     *
     * @param context: Context of MainActivity
     */
    public LambdaGetCallback(Context context) {
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
        activity.journalEntriesGetFailed(e.getMessage());
    }

    @Override
    public void onResponse(@NonNull Call call, Response response) throws IOException {

        // failed to get entries
        if (!response.isSuccessful()) {

            String msg = "Error: " + response.code();

            // add details if available
            if (null != response.body()) {
                msg += " - " + response.body().string();
            }

            activity.journalEntriesGetFailed(msg);
            return;
        }

        // list of all fetched entries
        List<JournalEntry> entries = new ArrayList<>();

        // no entries to process
        if (null == response.body()) {
            activity.journalEntriesGetSuccess(entries);
            return;
        }

        // get body as json string
        String json = response.body().string();

        try {
            JSONObject root = new JSONObject(json);
            JSONArray items = root.getJSONArray("entries");

            // convert json list to journal entry list
            for (int i = 0; i < items.length(); i++) {

                JSONObject item = items.getJSONObject(i);

                String mood = item.getString("mood");
                String notes = item.optString("notes", "");
                long timestamp = item.getLong("timestamp");

                entries.add(new JournalEntry(mood, notes, timestamp));
            }

            activity.journalEntriesGetSuccess(entries);
        }

        catch (JSONException e) {
            activity.journalEntriesGetFailed(e.getMessage());
        }
    }
}
