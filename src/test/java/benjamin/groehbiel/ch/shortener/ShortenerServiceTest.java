package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.ApplicationTest;
import benjamin.groehbiel.ch.PersistenceInitializer;
import benjamin.groehbiel.ch.DatabaseTest;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import benjamin.groehbiel.ch.shortener.wordnet.WordDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ShortenerServiceTest extends DatabaseTest {

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
    public void shouldUsePersistenceToLookUpHash() throws URISyntaxException, IOException {
        redisManager.setValue("$count", "0");

        assertThat(shortenerService.getShortenedCount(), equalTo(0L));

        redisManager.setValue("$count", "1");
        ShortenerHandle shortenerHandleForWater = new ShortenerHandle();
        String value = new ObjectMapper().writeValueAsString(shortenerHandleForWater);

        redisManager.setValue("water", value);

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
