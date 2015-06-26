package benjamin.groehbiel.ch.shortener.hash;

import benjamin.groehbiel.ch.SpringHashBasedApplicationTest;
import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;

public class HashBasedRepositoryTest extends SpringHashBasedApplicationTest {

    @Autowired
    ShortenerService shortenerService;

    @Test
    public void shouldGenerateAUniqueCodeForEachDistinctURI() throws URISyntaxException {
        URI inputUri = new URI("http://www.example.org");
        ShortenerHandle shortenerHandle = shortenerService.shorten(inputUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/a"));

        URI anotherShortenedUri = new URI("http://www.example2.org");
        shortenerHandle = shortenerService.shorten(anotherShortenedUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/b"));
    }

    @Test
    public void shouldReuseShortenedURIs() throws URISyntaxException {
        URI inputUri = new URI("http://www.example.org");
        ShortenerHandle shortenerHandle = shortenerService.shorten(inputUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/a"));

        shortenerHandle = shortenerService.shorten(inputUri);
        MatcherAssert.assertThat(shortenerHandle.getShortenedURI().toString(), Matchers.equalTo("http://www.shortener.com/a"));
    }

    @Test
    public void shouldDecodeShortURIToOriginalURI() throws Exception {
        URI inputUri = new URI("http://www.example.org");
        shortenerService.shorten(inputUri);

        URI expandedURI = shortenerService.expand("a");
        MatcherAssert.assertThat(expandedURI, equalTo(inputUri));
    }

}
