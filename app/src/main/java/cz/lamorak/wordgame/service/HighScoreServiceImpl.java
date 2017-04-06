package cz.lamorak.wordgame.service;

import android.content.Context;

import java.util.List;

import cz.lamorak.wordgame.model.Highscore;
import cz.lamorak.wordgame.observable.HighscoresObservable;
import io.reactivex.Observable;

/**
 * Created by ovancak on 06.04.2017.
 */

public class HighScoreServiceImpl implements HighscoreService {

    private final Context context;

    public HighScoreServiceImpl(final Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Highscore>> getHighscores() {
        return new HighscoresObservable(context);
    }
}
