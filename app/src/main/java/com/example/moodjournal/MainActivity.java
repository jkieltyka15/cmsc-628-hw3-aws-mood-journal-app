package com.example.moodjournal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LoginFragment loginFragment = new LoginFragment();

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

        // display login fragment
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_main, loginFragment)
                    .commit();
        }
    }
}