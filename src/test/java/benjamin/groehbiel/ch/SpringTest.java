package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.EnglishDictionary;
import benjamin.groehbiel.ch.shortener.WordDefinition;
import benjamin.groehbiel.ch.shortener.WordRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedList;
import java.util.List;

public class SpringTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    WordRepository wordRepository;

    @Autowired
    private EnglishDictionary englishDictionary;

    @Before
    public void clearRepository() {
        wordRepository.clear();

        List<WordDefinition> words = new LinkedList<>();
        words.add(new WordDefinition("fun", "enjoyment, amusement, or light-hearted pleasure."));
        words.add(new WordDefinition("eloquence", "fluent or persuasive speaking or writing."));
        words.add(new WordDefinition("elephant", "a very large plant-eating mammal with a prehensile trunk, long curved ivory tusks, and large ears, native to Africa and southern Asia."));
        englishDictionary.set(words);
    }
    
}