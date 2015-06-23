package benjamin.groehbiel.ch.shortener;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;

public class UrlShortenerServiceTest {

    ShortenerService shortenerService;

    @Before
    public void before() {
        shortenerService = new ShortenerService();
    }

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
