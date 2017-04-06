package cz.lamorak.wordgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import cz.lamorak.wordgame.model.Highscore;
import cz.lamorak.wordgame.model.Word;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static cz.lamorak.wordgame.ScreenUtil.getScreenHeight;

/**
 * Created by ondrej on 5.4.2017.
 */

public class GameActivity extends AppCompatActivity {

    private static final int TIME_LIMIT = 30; // seconds
    private static final int WORD_LIMIT = 3; // seconds

    private CompositeDisposable disposables;
    private Disposable wordTimerDisposable;
    private PublishSubject<Boolean> correctSubject;
    private PublishSubject<Boolean> guessSubject;
    private AtomicInteger timeRemaining;
    private AtomicBoolean gameStarted;
    private AtomicInteger score;
    private List<Word> words;
    private int screenHeight;

    private TextView scoreView;
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
        correctSubject = PublishSubject.create();
        guessSubject = PublishSubject.create();
        timeRemaining = new AtomicInteger(TIME_LIMIT);
        score = new AtomicInteger();
        gameStarted = new AtomicBoolean(false);
        screenHeight = getScreenHeight(this);

        scoreView = (TextView) findViewById(R.id.score);
        scoreView.setText(String.format(getString(R.string.game_score), score.get()));
        countdown = (TextView) findViewById(R.id.countdown);
        countdown.setText(String.valueOf(TIME_LIMIT));
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
                Observable.interval(1, TimeUnit.SECONDS)
                        .filter(timeElapsed -> gameStarted.get())
                        .take(timeRemaining.get())
                        .map(timeElapsed -> timeRemaining.decrementAndGet())
                        .map(String::valueOf)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(this::finishGame)
                        .subscribe(timeString -> countdown.setText(timeString))
        );
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
        disposables.add(
                correctSubject.zipWith(guessSubject, (correctAnswer, guess) -> correctAnswer == guess)
                        .map(answerCorrect -> {
                            if (answerCorrect) {
                                return score.incrementAndGet();
                            } else if (score.get() > 0) {
                                return score.decrementAndGet();
                            } else {
                                return score.get();
                            }
                        })
                        .map(score -> String.format(getString(R.string.game_score), score))
                        .subscribe(scoreView::setText)
        );
        if (gameStarted.get()) {
            displayWord();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.clear();
    }

    private void startGame(final List<Word> words) {
        this.words = words;
        gameStarted.set(true);
        displayWord();
    }

    private void displayWord() {
        if (words.size() < 2) {
            finishGame();
        }
        final Word word = words.remove(0);
        final Word wordWrong = words.remove(0);

        wordOriginal.setText(word.getEngllishWord());
        boolean isCorrect = new Random().nextBoolean();
        if (isCorrect) {
            wordGuess.setText(word.getSpanishWord());
        } else {
            wordGuess.setText(wordWrong.getSpanishWord());
        }
        correctSubject.onNext(isCorrect);

        wordGuess.setTranslationY(0);
        wordGuess.animate()
                .translationY(screenHeight)
                .setDuration(WORD_LIMIT * 1000)
                .start();

        if (wordTimerDisposable != null && !wordTimerDisposable.isDisposed()) {
            wordTimerDisposable.dispose();
        }
        wordTimerDisposable = Observable.timer(WORD_LIMIT, TimeUnit.SECONDS)
                .filter(l -> gameStarted.get())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l -> guessSubject.onNext(false));
        disposables.add(wordTimerDisposable);
    }

    private void finishGame() {
        gameStarted.set(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.game_dialog_title);

        final EditText input = new EditText(this);
        input.setHint(R.string.game_dialog_hint);
        builder.setView(input);
        builder.setPositiveButton(R.string.game_dialog_confirm, (dialog, which) -> {
            dialog.cancel();
            saveScoreAndFinish(input.getText().toString());
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void saveScoreAndFinish(final String name) {
        disposables.add(
                WordGameApp.getServiceProvider().getHighscoreService()
                        .updateHighscores(new Highscore(name, score.intValue()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(this::finish)
                        .subscribe()
        );
    }

    @Override
    public void onBackPressed() {
    }
}
