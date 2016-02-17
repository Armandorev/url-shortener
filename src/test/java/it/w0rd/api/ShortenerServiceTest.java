package it.w0rd.api;

import it.w0rd.DataTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ShortenerServiceTest extends DataTest {

    @Autowired
    private ShortenerService shortenerService;

    @Test
    public void shouldShortenUrl() throws URISyntaxException, IOException {
        ShortenedUrl shortenedUrl = shortenUrl("http://www.example.org");
        assertThat(shortenedUrl.getHash(), equalTo("able"));
    }

    @Test
    public void shouldReturnCorrectRemainingCount() {
        assertThat(shortenerService.getRemainingCount(), equalTo(21L));
    }

    @Test
    public void shouldReturnCorrectShortenedCount() {
        assertThat(shortenerService.getShortenedCount(), equalTo(0L));
    }

    @Test
    public void shouldRemoveExistingShortenedUrlAndFreeItUp() throws IOException, URISyntaxException {
        ShortenedUrl shortenedUrl = shortenUrl("http://www.example.com");

        MatcherAssert.assertThat(dictionaryManager.getWordsAvailableSize(), IsEqual.equalTo(20L));
        assertThat(shortenerService.getShortenedCount(), IsEqual.equalTo(1L));

        shortenerService.remove(shortenedUrl.getHash());

        MatcherAssert.assertThat(dictionaryManager.getWordsAvailableSize(), IsEqual.equalTo(21L));
        assertThat(shortenerService.getShortenedCount(), IsEqual.equalTo(0L));
    }

    private ShortenedUrl shortenUrl(String url) throws URISyntaxException, IOException {
        return shortenerService.shorten(new URI(url));
    }

}
