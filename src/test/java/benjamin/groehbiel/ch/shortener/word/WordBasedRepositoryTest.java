
package benjamin.groehbiel.ch.shortener.word;

import benjamin.groehbiel.ch.api.SpringWordBasedApplicationTest;
import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URISyntaxException;

public class WordBasedRepositoryTest extends SpringWordBasedApplicationTest {

    @Autowired
    ShortenerService shortenerService;

    @Test
    public void shouldGenerateAUniqueCodeForEachDistinctURI() throws URISyntaxException {
        URI inputUri = new URI("http://www.example.org");
        ShortenerHandle shortenerHandle = shortenerService.shorten(inputUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/eloquence"));

        URI anotherShortenedUri = new URI("http://www.example2.org");
        shortenerHandle = shortenerService.shorten(anotherShortenedUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/elephant"));
    }

}
