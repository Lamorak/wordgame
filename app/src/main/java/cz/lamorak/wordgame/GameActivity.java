package cz.lamorak.wordgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import cz.lamorak.wordgame.model.Word;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by ondrej on 5.4.2017.
 */

public class GameActivity extends AppCompatActivity {

    private static final int WORD_LIMIT = 3; // seconds

    private CompositeDisposable disposables;
    private Disposable wordTimerDisposable;
    private PublishSubject<Boolean> guessSubject;
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
        guessSubject = PublishSubject.create();

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
    protected void onResume() {
        super.onResume();
        disposables.add(
                RxView.clicks(wrongButton)
                        .map(o -> false)
                        .subscribe(guessSubject::onNext)
        );
        disposables.add(
                RxView.clicks(correctButton)
                        .map(o -> true)
                        .subscribe(guessSubject::onNext)
        );
        disposables.add(
                guessSubject.subscribe(aBoolean -> displayWord())
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.clear();
    }

    private void startGame(final List<Word> words) {
        this.words = words;
        displayWord();
    }

    private void displayWord() {
        if (words.size() < 2) {
            finishGame();
        }
        final Word word = words.remove(0);
        wordOriginal.setText(word.getEngllishWord());
        wordGuess.setText(word.getSpanishWord());
        wordGuess.setTranslationY(0);
        wordGuess.animate()
                .translationY(1000)
                .setDuration(3000L)
                .start();

        if (wordTimerDisposable != null && !wordTimerDisposable.isDisposed()) {
            wordTimerDisposable.dispose();
        }
        wordTimerDisposable = Observable.timer(WORD_LIMIT, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(l -> guessSubject.onNext(false));
        disposables.add(wordTimerDisposable);
    }
}
