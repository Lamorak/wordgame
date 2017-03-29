package cz.lamorak.wordgame.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ondrej on 29.3.2017.
 */

public class Word {

    @SerializedName("text_eng")
    private final String engllishWord;
    @SerializedName("text_spa")
    private final String spanishWord;

    public Word(String engllishWord, String spanishWord) {
        this.engllishWord = engllishWord;
        this.spanishWord = spanishWord;
    }

    public String getEngllishWord() {
        return engllishWord;
    }

    public String getSpanishWord() {
        return spanishWord;
    }
}
