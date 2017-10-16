package edu.orangecoastcollege.cs273.dtran258.cs273superheroes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TextView mQuestionNumberTextView;
    private ImageView mHeroImageView;
    private TextView mGuessTextView;
    private Button[] mButtons = new Button[4];
    private TextView mAnswerTextView;

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

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mHeroImageView = (ImageView) findViewById(R.id.heroImageView);
        mGuessTextView = (TextView) findViewById(R.id.guessTextView);
        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
    }
}
