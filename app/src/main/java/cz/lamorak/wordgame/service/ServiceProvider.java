package cz.lamorak.wordgame.service;

import android.content.Context;

/**
 * Created by ondrej on 5.4.2017.
 */

public class ServiceProvider {

    private final Context context;

    public ServiceProvider(final Context context) {
        this.context = context;
    }

    public WordService getWordService() {
        return new WordServiceImpl(context);
    }
}
