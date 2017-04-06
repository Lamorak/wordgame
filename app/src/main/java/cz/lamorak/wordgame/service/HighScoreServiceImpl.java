package cz.lamorak.wordgame.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import cz.lamorak.wordgame.model.Highscore;
import cz.lamorak.wordgame.observable.HighscoresObservable;
import io.reactivex.Observable;

/**
 * Created by ovancak on 06.04.2017.
 */

public class HighScoreServiceImpl implements HighscoreService {

    private static final int HIGHSCORE_COUNT = 5;

    private final Context context;

    public HighScoreServiceImpl(final Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Highscore>> getHighscores() {
        return new HighscoresObservable(HIGHSCORE_COUNT, context);
    }

    @Override
    public Observable<List<Highscore>> updateHighscores(final Highscore newHighscore) {
        return getHighscores()
                .map(highscores -> {
                    int index = highscores.size();
                    for (Highscore highscore : highscores) {
                        if (newHighscore.getValue() > highscore.getValue()) {
                            index = highscores.indexOf(highscore);
                            break;
                        }
                    }
                    if (index < highscores.size()) {
                        highscores.add(index, newHighscore);
                        highscores.remove(highscores.size() - 1);
                    }
                    return highscores;
                }).doOnNext(this::saveHighscores);
    }

    private void saveHighscores(final List<Highscore> highscores) {
        SharedPreferences.Editor editor = context.getSharedPreferences("highscores", Context.MODE_PRIVATE).edit();
        for (int i = 0; i < HIGHSCORE_COUNT; i++) {
            editor.putString(Highscore.KEY_NAME + i, highscores.get(i).getName());
            editor.putInt(Highscore.KEY_VALUE + i, highscores.get(i).getValue());
        }
        editor.apply();
    }
}
