package cz.lamorak.wordgame;

import android.app.Application;

import cz.lamorak.wordgame.service.ServiceProvider;

/**
 * Created by ondrej on 5.4.2017.
 */

public class WordGameApp extends Application {

    private static ServiceProvider serviceProvider;

    @Override
    public void onCreate() {
        super.onCreate();

        serviceProvider = new ServiceProvider(this);
    }

    public static ServiceProvider getServiceProvider() {
        return serviceProvider;
    }
}
