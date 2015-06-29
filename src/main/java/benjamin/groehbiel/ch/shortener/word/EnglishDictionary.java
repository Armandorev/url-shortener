package benjamin.groehbiel.ch.shortener.word;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Repository
public class EnglishDictionary {

    private List<WordDefinition> words;

    public EnglishDictionary () throws IOException {
        words = load();
    }

    public WordDefinition get() {
        return words.remove(0);
    }

    public void set(List<WordDefinition> words) {
        this.words = words;
    }

    public void clear() {
        words.clear();
    }

    public int size() {
        return words.size();
    }

    public List<WordDefinition> load() throws IOException {
        List<WordDefinition> englishWords = WordNetHelper.load("src/main/resources/WordNet");
        Collections.shuffle(englishWords);
        return englishWords;
    }
}
