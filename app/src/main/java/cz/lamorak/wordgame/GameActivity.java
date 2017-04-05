package cz.lamorak.wordgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ondrej on 5.4.2017.
 */

public class GameActivity extends AppCompatActivity {

    private TextView countdown;
    private TextView wordOriginal;
    private TextView wordGuess;
    private Button wrongButton;
    private Button correctButton;

    public static void call(final Context context) {
        final Intent intent = new Intent(context, GameActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        countdown = (TextView) findViewById(R.id.countdown);
        wordOriginal = (TextView) findViewById(R.id.word_original);
        wordGuess = (TextView) findViewById(R.id.word_guess);
        correctButton = (Button) findViewById(R.id.button_correct);
        wrongButton = (Button) findViewById(R.id.button_wrong);
    }
}
