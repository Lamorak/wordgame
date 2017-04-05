package cz.lamorak.wordgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import cz.lamorak.wordgame.model.Word;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
/**
 * Created by ondrej on 5.4.2017.
 */

public class GameActivity extends AppCompatActivity {

    private CompositeDisposable disposables;
    private List<Word> words;

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

        disposables = new CompositeDisposable();

        countdown = (TextView) findViewById(R.id.countdown);
        wordOriginal = (TextView) findViewById(R.id.word_original);
        wordGuess = (TextView) findViewById(R.id.word_guess);
        correctButton = (Button) findViewById(R.id.button_correct);
        wrongButton = (Button) findViewById(R.id.button_wrong);

        disposables.add(
                WordGameApp.getServiceProvider()
                        .getWordService()
                        .loadWords()
                        .doOnNext(Collections::shuffle)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::startGame)
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.clear();
    }

    private void startGame(final List<Word> words) {
        this.words = words;
    }
}
