package cz.lamorak.wordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private CompositeDisposable disposables;

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disposables = new CompositeDisposable();
        startButton = (Button) findViewById(R.id.button_start);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
