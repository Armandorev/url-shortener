package benjamin.groehbiel.ch;

import java.util.HashMap;
import java.util.Map;

public class Base10Alphabet implements Alphabet {

    private static final Map<Integer, Character> indexToLetter;
    private static final Map<Character, Integer> letterToIndex;

    static {
        indexToLetter = new HashMap<>();
        indexToLetter.put(0, 'a');
        indexToLetter.put(1, 'b');
        indexToLetter.put(2, 'c');
        indexToLetter.put(3, 'd');
        indexToLetter.put(4, 'e');
        indexToLetter.put(5, 'f');
        indexToLetter.put(6, 'g');
        indexToLetter.put(7, 'h');
        indexToLetter.put(8, 'i');
        indexToLetter.put(9, 'j');

        letterToIndex = new HashMap<>();
        letterToIndex.put('a', 0);
        letterToIndex.put('b', 1);
        letterToIndex.put('c', 2);
        letterToIndex.put('d', 3);
        letterToIndex.put('e', 4);
        letterToIndex.put('f', 5);
        letterToIndex.put('g', 6);
        letterToIndex.put('h', 7);
        letterToIndex.put('i', 8);
        letterToIndex.put('j', 9);
    }

    public Character getLetterFor(Integer index) {
        return indexToLetter.get(index);
    }

    public Integer getIndexFor(Character character) {
        return letterToIndex.get(character);
    }

}
