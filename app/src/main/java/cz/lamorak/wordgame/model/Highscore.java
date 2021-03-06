package cz.lamorak.wordgame.model;

/**
 * Created by ovancak on 06.04.2017.
 */

public class Highscore {

    public static final String KEY_NAME = "highscore_name_";
    public static final String KEY_VALUE = "highscore_value_";

    private final String name;
    private final int value;

    public Highscore(final String name, final int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
