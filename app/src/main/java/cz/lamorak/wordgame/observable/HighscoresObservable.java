package cz.lamorak.wordgame.observable;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import cz.lamorak.wordgame.model.Highscore;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by ovancak on 06.04.2017.
 */

public class HighscoresObservable extends Observable<List<Highscore>> {

    public static final int HIGHSCORE_COUNT = 5;

    private final Context context;

    public HighscoresObservable(final Context context) {
        this.context = context;
    }

    @Override
    protected void subscribeActual(final Observer<? super List<Highscore>> observer) {
        SharedPreferences preferences = context.getSharedPreferences("highscores", Context.MODE_PRIVATE);
        final List<Highscore> highscores = new ArrayList<>(HIGHSCORE_COUNT);
        for (int i = 0; i < HIGHSCORE_COUNT; i++) {
            final String name = preferences.getString("highscore_name_" + i, "empty highscore");
            final int value = preferences.getInt("highscore_value_" + i, 0);
            highscores.add(new Highscore(name, value));
        }
        observer.onNext(highscores);
        observer.onComplete();
    }
}
