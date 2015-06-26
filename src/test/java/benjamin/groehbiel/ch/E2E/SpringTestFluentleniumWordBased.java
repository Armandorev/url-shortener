package benjamin.groehbiel.ch.E2E;

import benjamin.groehbiel.ch.ApplicationWordBased;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import benjamin.groehbiel.ch.shortener.word.WordBasedRepository;
import benjamin.groehbiel.ch.shortener.word.WordDefinition;
import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationWordBased.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public abstract class SpringTestFluentleniumWordBased extends FluentTest {

    @Autowired
    private ShortenerRepository shortenerRepository;

    @Before
    public void clearRepository() {
        shortenerRepository.clear();

        List<WordDefinition> words = new LinkedList<>();
        words.add(new WordDefinition("fun", "enjoyment, amusement, or light-hearted pleasure."));
        words.add(new WordDefinition("eloquence", "fluent or persuasive speaking or writing."));
        words.add(new WordDefinition("elephant", "a very large plant-eating mammal with a prehensile trunk, long curved ivory tusks, and large ears, native to Africa and southern Asia."));

        WordBasedRepository wordBasedRepository = (WordBasedRepository) this.shortenerRepository;

        wordBasedRepository.setDictionary(words);
    }

}
