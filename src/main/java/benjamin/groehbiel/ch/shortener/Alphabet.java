package benjamin.groehbiel.ch.shortener;

import java.util.Map;

public interface Alphabet {

    Map<Integer, Character> indexToLetter = null;
    Map<Character, Integer> letterToIndex = null;

    Character getLetterFor(Integer id);
    Integer getIndexFor(Character character);

}