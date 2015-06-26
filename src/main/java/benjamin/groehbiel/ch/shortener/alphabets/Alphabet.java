package benjamin.groehbiel.ch.shortener.alphabets;

public interface Alphabet {

    Character getLetterFor(Integer id);
    Integer getIndexFor(Character character);

}
