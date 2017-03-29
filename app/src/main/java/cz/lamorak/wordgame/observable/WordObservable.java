package cz.lamorak.wordgame.observable;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;

import cz.lamorak.wordgame.model.Word;
import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by ondrej on 29.3.2017.
 */

public class WordObservable extends Observable<Word> {

    private final Context context;
    private final String filename;

    public WordObservable(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }

    @Override
    protected void subscribeActual(Observer<? super Word> observer) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(context.getAssets().open(filename));
            Word[] words = new Gson().fromJson(inputStreamReader, Word[].class);
            for (Word word : words) {
                observer.onNext(word);
            }
        } catch (IOException e) {
            observer.onError(e);
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            observer.onComplete();
        }
    }
}
