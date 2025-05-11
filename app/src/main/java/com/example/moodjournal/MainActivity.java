package com.example.moodjournal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private final LoginFragment loginFragment = new LoginFragment();
    private final SignupFragment signupFragment = new SignupFragment();


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

            // display dashboard fragment
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
        }

        // signup text clicked in login fragment
        else if (R.id.textView_signup == elementId) {
            handler.post(new ChangeFragmentWork(signupFragment));
        }

        // signup button clicked in signup fragment
        else if (R.id.button_signup == elementId) {
            // @todo Implement logging into AWS
        }

        // login text clicked in signup fragment
        else if (R.id.textView_login == elementId) {
            handler.post(new ChangeFragmentWork(loginFragment));
        }
    }
}
