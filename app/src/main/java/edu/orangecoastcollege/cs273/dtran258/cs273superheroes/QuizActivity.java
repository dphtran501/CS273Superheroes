package edu.orangecoastcollege.cs273.dtran258.cs273superheroes;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
 *     The user can switch between guessing the name, the super power, or the one thing about the
 *     super hero by clicking on the settings options menu at the top.
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
    private List<Superhero> mQuizSuperheroesList;
    // Quiz fields
    private Superhero mCorrectSuperhero;
    private int mTotalGuesses;
    private int mCorrectGuesses;

    private SecureRandom rng;
    private Handler handler;


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

        mQuizSuperheroesList = new ArrayList<>(SUPERHEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        // Start quiz
        resetQuiz();
    }

    private void resetQuiz()
    {
        // Clear quiz fields
        mTotalGuesses = 0;
        mCorrectGuesses = 0;
        mQuizSuperheroesList.clear();

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

        // Set each of the four buttons to a random superhero from the all superheroes list
        do
        {
            Collections.shuffle(mAllSuperheroesList);
        } while(mAllSuperheroesList.subList(0, 4).contains(mCorrectSuperhero));

        for (int i = 0; i < 4; i++)
        {
            mButtons[i].setEnabled(true);
            mButtons[i].setText(mAllSuperheroesList.get(i).getUserName());
        }

        // Randomly set one of the buttons to the correct superhero
        mButtons[rng.nextInt(4)].setText(mCorrectSuperhero.getUserName());

    }
}
