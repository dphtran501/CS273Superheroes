package edu.orangecoastcollege.cs273.dtran258.cs273superheroes;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * This activity allows the user to specify what type of quiz to run in <code>QuizActivity</code>.
 *
 * @author Derek Tran
 * @version 1.0
 * @since October 17, 2017
 */

public class SettingsActivity extends AppCompatActivity
{
    /**
     * Initializes <code>SettingsActivity</code> by inflating its UI.
     *
     * @param savedInstanceState Bundle containing the data it recently supplied in
     *                           onSaveInstanceState(Bundle) if activity was reinitialized after
     *                           being previously shut down. Otherwise it is null.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Enable home button (not on by default)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Use fragment to fill out content (replaces existing fragment)
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsActivityFragment()).commit();
    }

    /**
     * Shows a hierarchy of <code>Preference</code> objects as lists. Used to deal with preferences
     * in applications.
     */
    public static class SettingsActivityFragment extends PreferenceFragment
    {
        // Inflates preferences GUI from preferences.xml
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
