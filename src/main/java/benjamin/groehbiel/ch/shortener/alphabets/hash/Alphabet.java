package benjamin.groehbiel.ch.shortener.alphabets.hash;

public interface Alphabet {

    Character getLetterFor(Integer id);
    Integer getIndexFor(Character character);

}
