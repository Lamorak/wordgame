package cz.lamorak.wordgame.service;

import java.util.List;

import cz.lamorak.wordgame.model.Highscore;
import io.reactivex.Observable;

/**
 * Created by ovancak on 06.04.2017.
 */

public interface HighscoreService {

    Observable<List<Highscore>> getHighscores();
}
