package it.w0rd.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.w0rd.DataTest;
import it.w0rd.persistence.redis.RedisManager;
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
        ShortenerHandle shortenerHandle = shortenUrl("http://www.example.org");
        assertThat(shortenerHandle.getHash(), equalTo("able"));
    }

    @Test
    public void shouldLookupHash() throws URISyntaxException, IOException {
        String value = new ObjectMapper().writeValueAsString(new ShortenerHandle());
        redisManager.setUrlAndHash(RedisManager.HASH_PREFIX + "water", value);

        ShortenerHandle shortenerHandle = shortenerService.expand("water");
        assertThat(shortenerHandle, equalTo(new ShortenerHandle()));
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
        ShortenerHandle shortenedUrl = shortenUrl("http://www.example.com");

        MatcherAssert.assertThat(dictionaryManager.getWordsAvailableSize(), IsEqual.equalTo(20L));
        assertThat(shortenerService.getShortenedCount(), IsEqual.equalTo(1L));

        shortenerService.remove(shortenedUrl.getHash());

        MatcherAssert.assertThat(dictionaryManager.getWordsAvailableSize(), IsEqual.equalTo(21L));
        assertThat(shortenerService.getShortenedCount(), IsEqual.equalTo(0L));
    }

    private ShortenerHandle shortenUrl(String url) throws URISyntaxException, IOException {
        return shortenerService.shorten(new URI(url));
    }

}
