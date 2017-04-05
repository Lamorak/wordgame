package cz.lamorak.wordgame.service;

import java.util.List;

import cz.lamorak.wordgame.model.Word;
import io.reactivex.Observable;

/**
 * Created by ondrej on 5.4.2017.
 */

public interface WordService {

    Observable<List<Word>> loadWords();
}
