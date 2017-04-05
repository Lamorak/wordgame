package cz.lamorak.wordgame.service;

import android.content.Context;

import java.util.List;

import cz.lamorak.wordgame.model.Word;
import cz.lamorak.wordgame.observable.WordObservable;
import io.reactivex.Observable;

/**
 * Created by ondrej on 29.3.2017.
 */

public class WordServiceImpl implements WordService {

    private final Context context;

    public WordServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Word>> loadWords() {
        return new WordObservable(context, "words.json");
    }
}
