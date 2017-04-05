package cz.lamorak.wordgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ondrej on 5.4.2017.
 */

public class GameActivity extends AppCompatActivity {

    public static void call(final Context context) {
        final Intent intent = new Intent(context, GameActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}
