package benjamin.groehbiel.ch;

import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.equalTo;

public class UrlShortenerService {

    @Ignore
    @Test
    public void shouldShortenUri() throws URISyntaxException {
        URI inputUri = new URI("http://www.example.org");
    }

    @Ignore
    @Test
    public void shouldDecodeShortURIToOriginalURI() throws Exception {
        URI inputUri = new URI("http://www.example.org");
    }

}
