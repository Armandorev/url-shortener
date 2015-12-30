package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.DataTest;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
import com.fasterxml.jackson.databind.ObjectMapper;
<<<<<<< HEAD
=======
import org.junit.Before;
>>>>>>> Add admin api for listing all available hashes including paging/sorting
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Ignore
public class ShortenerServiceTest extends DataTest {

    @Autowired
    private ShortenerService shortenerService;

    @Autowired
    private RedisManager redisManager;

    @Test
    public void shouldReturnARealWordAsHashInShortenedUrl() throws URISyntaxException, IOException {
        URI inputUri = new URI("http://www.example.org");
        ShortenerHandle shortenerHandle = shortenerService.shorten(inputUri);
        assertThat(shortenerHandle.getHash(), equalTo("able"));

        URI anotherShortenedUri = new URI("http://www.example2.org");
        shortenerHandle = shortenerService.shorten(anotherShortenedUri);
        assertThat(shortenerHandle.getHash(), equalTo("unable"));
    }


    @Test
    public void shouldLookupUrlInRedisGivenAHash() throws URISyntaxException, IOException {
        redisManager.setUrlAndHash("$count", "0");

        assertThat(shortenerService.getShortenedCount(), equalTo(0L));

        redisManager.setUrlAndHash("$count", "1");
        ShortenerHandle shortenerHandleForWater = new ShortenerHandle();
        String value = new ObjectMapper().writeValueAsString(shortenerHandleForWater);

        redisManager.setUrlAndHash(RedisManager.HASH_PREFIX + "water", value);

        ShortenerHandle shortenerHandle = shortenerService.expand("water");
        assertThat(shortenerHandle, equalTo(shortenerHandleForWater));
    }

    @Test
    public void shouldReturnCorrectRemainingCount() {
        assertThat(shortenerService.getRemainingCount(), equalTo(21L));
    }

    @Test
    public void shouldReturnCorrectShortenedCount() {
        assertThat(shortenerService.getShortenedCount(), equalTo(0L));
    }

}
