package benjamin.groehbiel.ch.shortener.word;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.File;
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
        File wordNet = new ClassPathResource("WordNet").getFile();
        List<WordDefinition> englishWords = WordNetHelper.load(wordNet.getPath());
        Collections.shuffle(englishWords);
        return englishWords;
    }
}
