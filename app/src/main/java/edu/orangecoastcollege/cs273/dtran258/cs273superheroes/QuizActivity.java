package edu.orangecoastcollege.cs273.dtran258.cs273superheroes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runs an application that quizzes the user on the "superheroes" of CS 273. An image of a
 * "superhero" is displayed to the screen, as well as four buttons each with a information that
 * might be related to the "superhero" shown. The user has to click the button with the information
 * related to the "superhero" shown. In the end, the user will be shown a percentage indicating how
 * well they did on the quiz.
 * <p>
 * The user can switch between guessing the name, the super power, or the one thing about the
 * super hero by clicking on the settings options menu at the top.
 * </p>
 *
 * @author Derek Tran
 * @version 1.0
 * @since October 15, 2017
 */
public class QuizActivity extends AppCompatActivity
{
    private static final String TAG = "CS 273 Superheroes";
    private static final int SUPERHEROES_IN_QUIZ = 10;

    // Views
    private TextView mQuestionNumberTextView;
    private ImageView mSuperheroImageView;
    private TextView mGuessTextView;
    private Button[] mButtons = new Button[4];
    private TextView mAnswerTextView;
    // Lists
    private List<Superhero> mAllSuperheroesList;
    private List<String> mAllSuperheroNamesList;
    private List<String> mAllSuperheroPowersList;
    private List<String> mAllSuperheroOneThingsList;
    private List<String> mAllSuperheroTypeList;
    private List<Superhero> mQuizSuperheroesList;
    // Quiz fields
    private Superhero mCorrectSuperhero;
    private String mCorrectAnswer;
    private int mTotalGuesses;
    private int mCorrectGuesses;

    private SecureRandom rng;
    private Handler handler;

    // Preferences
    private String mQuizType;
    private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
            // Read quiz type from shared activity_settings
            mQuizType = sharedPreferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));
            resetQuiz();

            // Notify user that quiz will restart
            Toast.makeText(QuizActivity.this, R.string.restart_toast, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Initializes <code>QuizActivity</code> by inflating its UI.
     *
     * @param savedInstanceState Bundle containing the data it recently supplied in
     *                           onSaveInstanceState(Bundle) if activity was reinitialized after
     *                           being previously shut down. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get resources from GUI components
        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mSuperheroImageView = (ImageView) findViewById(R.id.heroImageView);
        mGuessTextView = (TextView) findViewById(R.id.guessTextView);
        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        // Load list of all Superheroes from JSON file
        try
        {
            mAllSuperheroesList = JSONLoader.loadJSONFromAsset(this);
        } catch (IOException e)
        {
            Log.e(TAG, "Error loading from JSON", e);
        }

        // Initialize other lists
        mAllSuperheroNamesList = new ArrayList<>();
        mAllSuperheroPowersList = new ArrayList<>();
        mAllSuperheroOneThingsList = new ArrayList<>();
        for (Superhero s : mAllSuperheroesList)
        {
            mAllSuperheroNamesList.add(s.getName());
            mAllSuperheroPowersList.add(s.getSuperPower());
            mAllSuperheroOneThingsList.add(s.getOneThing());
        }
        mQuizSuperheroesList = new ArrayList<>(SUPERHEROES_IN_QUIZ);

        rng = new SecureRandom();
        handler = new Handler();

        // Register mPreferenceChangeListener
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
        // Update quiz based on activity_settings
        mQuizType = preferences.getString(getString(R.string.pref_key), getString(R.string.pref_default));

        // Start quiz
        resetQuiz();
    }

    private void resetQuiz()
    {
        // Clear quiz fields
        mTotalGuesses = 0;
        mCorrectGuesses = 0;
        mQuizSuperheroesList.clear();
        // Display question prompt
        mGuessTextView.setText(getString(R.string.guess, mQuizType));

        // Randomly add Superheroes from all Superheroes list to quiz list
        while (mQuizSuperheroesList.size() < SUPERHEROES_IN_QUIZ)
        {
            int randomPosition = rng.nextInt(mAllSuperheroesList.size());
            if (!mQuizSuperheroesList.contains(mAllSuperheroesList.get(randomPosition)))
                mQuizSuperheroesList.add(mAllSuperheroesList.get(randomPosition));
        }

        // Load quiz question
        loadNextSuperhero();
    }

    private void loadNextSuperhero()
    {
        // Retrieve correct superhero
        mCorrectSuperhero = mQuizSuperheroesList.remove(0);
        // Clear answer TextView so it doesn't show text from previous question
        mAnswerTextView.setText("");
        // Display current question number
        int questionNumber = SUPERHEROES_IN_QUIZ - mQuizSuperheroesList.size();
        mQuestionNumberTextView.setText(getString(R.string.question, questionNumber, SUPERHEROES_IN_QUIZ));

        // Display image of correct superhero
        try
        {
            InputStream stream = getAssets().open(mCorrectSuperhero.getFileName());
            Drawable image = Drawable.createFromStream(stream, mCorrectSuperhero.getUserName());
            mSuperheroImageView.setImageDrawable(image);
        } catch (IOException e)
        {
            Log.e(TAG, "Error loading image: " + mCorrectSuperhero.getFileName(), e);
        }

        // Update which superhero information to use for quiz according to activity_settings
        if (mQuizType.equals(getString(R.string.name_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroNamesList);
            mCorrectAnswer = mCorrectSuperhero.getName();
        } else if (mQuizType.equals(getString(R.string.power_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroPowersList);
            mCorrectAnswer = mCorrectSuperhero.getSuperPower();
        } else if (mQuizType.equals(getString(R.string.one_thing_type)))
        {
            mAllSuperheroTypeList = new ArrayList<>(mAllSuperheroOneThingsList);
            mCorrectAnswer = mCorrectSuperhero.getOneThing();
        }

        // Set buttons to preference-based superhero information
        do
        {
            Collections.shuffle(mAllSuperheroTypeList);
        } while (mAllSuperheroTypeList.subList(0, mButtons.length).contains(mCorrectAnswer));

        for (int i = 0; i < mButtons.length; i++)
        {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllSuperheroTypeList.get(i));
        }
        // Randomly select one of the buttons to have the correct answer
        mButtons[rng.nextInt(mButtons.length)].setText(mCorrectAnswer);

    }

    /**
     * Handles the click event of one of the buttons indicating the guess of a superhero's
     * information (name, superpower, or one thing about them) to match the superhero image
     * displayed. If the guess is correct, the superhero's information (in GREEN) will be shown,
     * followed by a slight delay of 2 seconds, then the next superhero will be loaded. Otherwise,
     * "Incorrect!" will be shown (in RED) and the button will be disabled.
     *
     * @param v The view that called this method.
     */
    public void makeGuess(View v)
    {
        // Retrieve text of clicked button (guess)
        Button clickedButton = (Button) v;
        String guess = clickedButton.getText().toString();

        mTotalGuesses++;

        // If guess is correct, increment number of correct guesses, disable all buttons, and
        // display correct answer in green text
        if (guess.equals(mCorrectAnswer))
        {
            mCorrectGuesses++;
            for (Button b : mButtons) b.setEnabled(false);
            mAnswerTextView.setText(mCorrectAnswer);
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));

            // If user hasn't completed quiz, load next question
            if (mCorrectGuesses < SUPERHEROES_IN_QUIZ)
            {
                // Wait two seconds before displaying next question
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadNextSuperhero();
                    }
                }, 2000);
            }
            // Else, user completed quiz, so show AlertDialog w/statistics and option to reset quiz
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.results, mTotalGuesses, 100.0 * mCorrectGuesses / mTotalGuesses));
                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        resetQuiz();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        }
        // Else, guess is incorrect, so disable clicked button and display "Incorrect!" in red text
        else
        {
            clickedButton.setEnabled(false);
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
        }
    }

    /**
     * Initializes the contents of this activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be
     * shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Called whenever an item in your options menu is selected.
     *
     * @param item The menu item selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Launch SettingsActivity
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);

        return super.onOptionsItemSelected(item);
    }
}
