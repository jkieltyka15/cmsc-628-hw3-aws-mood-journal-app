package com.example.moodjournal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class SignupFragment extends Fragment implements View.OnClickListener {

    private View.OnClickListener callback;  // callback to view when ui element is clicked


    /**
     * Called when fragment is attached
     *
     * @param context: Context
     */
    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);

        // setup callback
        if (context instanceof View.OnClickListener) {
            callback = (View.OnClickListener) context;
        }

        // context must implement OnClickListener
        else {
            throw new ClassCastException(context.toString()
                    + " must implement OnClickListener");
        }
    }


    /**
     * Called when fragment view is created
     *
     * @param inflater: Used to inflate any views in the fragment
     * @param container: Parent view that the fragment UI should be attached to
     * @param savedInstanceState: Previous saved state
     *
     * @return View of fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_signup, container, false);

        // initialize clickable elements
        Button signupButton = view.findViewById(R.id.button_signup);
        TextView loginTextView = view.findViewById(R.id.textView_login);

        // setup clickable element listeners
        signupButton.setOnClickListener(this);
        loginTextView.setOnClickListener(this);

        return view;
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

        // perform callback
        if (null != callback) {

            // signup button clicked
            if (R.id.button_signup == elementId) {
                callback.onClick(view);
            }

            // login text clicked
            else if (R.id.textView_login == elementId) {
                callback.onClick(view);
            }
        }
    }
}