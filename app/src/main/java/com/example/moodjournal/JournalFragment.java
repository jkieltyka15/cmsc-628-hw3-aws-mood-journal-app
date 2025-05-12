package com.example.moodjournal;

import static com.example.moodjournal.R.*;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;


public class JournalFragment extends Fragment implements View.OnClickListener {

    private final Handler handler = new Handler();  // handles UI changes safely

    private View.OnClickListener callback;  // callback to view when ui element is clicked

    private TextView textViewToolbarTitle;  // shows toolbar title

    private final EntryFragment entryFragment = new EntryFragment();
    private final HistoryFragment historyFragment = new HistoryFragment();


    private class ChangeFragmentWork implements Runnable {

        int titleStringId;
        Fragment fragment;

        /**
         * Constructs a new ChangeFragmentWork object
         *
         * @param titleStringId: ID of string to update the toolbar title to
         * @param fragment: Fragment to replace the current fragment that populates
         *                the fragment container
         */
        ChangeFragmentWork(int titleStringId, Fragment fragment) {

            this.titleStringId = titleStringId;
            this.fragment = fragment;
        }


        /**
         * Replaces the current fragment that is being displayed and updates
         * the toolbar title
         */
        @Override
        public void run() {

            // get current toolbar title
            String titleCurrent = textViewToolbarTitle.getText().toString();

            // get the dashboard title string
            String titleNext = getString(titleStringId);

            // already displaying fragment
            if (titleCurrent.equals(titleNext)) {
                return;
            }

            // update toolbar title
            textViewToolbarTitle.setText(titleNext);

            // display fragment
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_journal, fragment)
                    .commit();
        }
    }


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
            throw new ClassCastException(context + " must implement OnClickListener");
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        // setup toolbar title
        textViewToolbarTitle = view.findViewById(R.id.textView_toolbar_title);
        textViewToolbarTitle.setText(R.string.toolbar_menu_entry);

        // setup toolbar menu button
        ImageButton buttonToolbarMenu = view.findViewById(R.id.button_toolbar_menu);
        buttonToolbarMenu.setOnClickListener(this);

        // display entry fragment
        if (null == savedInstanceState) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_journal, entryFragment)
                    .commit();
        }

        // inflate the layout for this fragment
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

        // open toolbar menu
        if (elementId == R.id.button_toolbar_menu) {

            // create a popup menu anchored to toolbar menu button
            PopupMenu popup = new PopupMenu(getContext(), view);

            // inflate the popup menu
            popup.getMenuInflater().inflate(R.menu.toolbar_menu, popup.getMenu());

            // set a click listener for menu items
            popup.setOnMenuItemClickListener(item -> {

                // get ID of menu item clicked
                int itemId = item.getItemId();

                // navigate to create entry
                if (R.id.toolbar_menu_entry == itemId) {
                    handler.post(new ChangeFragmentWork(R.string.toolbar_menu_entry, entryFragment));
                    return true;
                }

                // navigate to history
                if (R.id.toolbar_menu_history == itemId) {
                    handler.post(new ChangeFragmentWork(R.string.toolbar_menu_history, historyFragment));
                    return true;
                }

                // sign out
                if (id.toolbar_menu_sign_out == itemId) {

                    if (null != callback) {

                        // simulate a fake View with the correct ID
                        View fakeView = new View(getContext());
                        fakeView.setId(R.id.toolbar_menu_sign_out);
                        callback.onClick(fakeView);
                    }

                    return true;
                }

                // unknown selection
                return false;
            });

            // show popup menu
            popup.show();
        }
    }
}