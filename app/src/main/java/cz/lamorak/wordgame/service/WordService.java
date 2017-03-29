package cz.lamorak.wordgame.service;

import android.content.Context;

import cz.lamorak.wordgame.model.Word;
import cz.lamorak.wordgame.observable.WordObservable;
import io.reactivex.Observable;

/**
 * Created by ondrej on 29.3.2017.
 */

public class WordService {

    private final Context context;

    public WordService(Context context) {
        this.context = context;
    }

    public Observable<Word> loadWords() {
        return new WordObservable(context, "words.json");
    }
}
