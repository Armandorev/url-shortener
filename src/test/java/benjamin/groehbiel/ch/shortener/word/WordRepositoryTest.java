
package benjamin.groehbiel.ch.shortener.word;

import benjamin.groehbiel.ch.ApplicationTest;
import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class WordRepositoryTest {

    @Autowired
    private ShortenerService shortenerService;

    @Autowired
    private EnglishDictionary englishDictionary;

    @Autowired
    ShortenerRepository shortenerRepository;

    @Before
    public void clearRepository() {
        shortenerRepository.clear();

        List<WordDefinition> words = new LinkedList<>();
        words.add(new WordDefinition("fun", "enjoyment, amusement, or light-hearted pleasure."));
        words.add(new WordDefinition("eloquence", "fluent or persuasive speaking or writing."));
        words.add(new WordDefinition("elephant", "a very large plant-eating mammal with a prehensile trunk, long curved ivory tusks, and large ears, native to Africa and southern Asia."));
        englishDictionary.set(words);
    }

    @Test
    public void shouldReturnARealWordAsHashInShortenedUrl() throws URISyntaxException {
        URI inputUri = new URI("http://www.example.org");
        ShortenerHandle shortenerHandle = shortenerService.shorten(inputUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/fun"));

        URI anotherShortenedUri = new URI("http://www.example2.org");
        shortenerHandle = shortenerService.shorten(anotherShortenedUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/eloquence"));
    }

}
