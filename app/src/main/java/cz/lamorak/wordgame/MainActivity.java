package cz.lamorak.wordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;

import cz.lamorak.wordgame.adapter.HighscoreAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable disposables;

    private RecyclerView highscoreRecycler;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disposables = new CompositeDisposable();
        highscoreRecycler = (RecyclerView) findViewById(R.id.highscore_recycler);
        highscoreRecycler.setLayoutManager(new LinearLayoutManager(this));
        startButton = (Button) findViewById(R.id.button_start);
    }

    @Override
    protected void onResume() {
        super.onResume();
        disposables.add(
                WordGameApp.getServiceProvider().getHighscoreService()
                        .getHighscores()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(HighscoreAdapter::new)
                        .subscribe(highscoreRecycler::setAdapter)
        );
        disposables.add(
                RxView.clicks(startButton)
                        .subscribe(v -> GameActivity.call(this))
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        disposables.clear();
    }
}
