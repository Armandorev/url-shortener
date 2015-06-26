package benjamin.groehbiel.ch.shortener;

public interface Alphabet {

    Character getLetterFor(Integer id);
    Integer getIndexFor(Character character);

}
