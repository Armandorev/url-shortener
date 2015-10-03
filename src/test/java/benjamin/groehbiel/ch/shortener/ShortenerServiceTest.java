package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.ApplicationTest;
import benjamin.groehbiel.ch.SpringTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
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

import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ShortenerServiceTest extends SpringTest {

    @Autowired
    private ShortenerService shortenerService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void shouldReturnARealWordAsHashInShortenedUrl() throws URISyntaxException, IOException {
        URI inputUri = new URI("http://www.example.org");
        ShortenerHandle shortenerHandle = shortenerService.shorten(inputUri);
        MatcherAssert.assertThat(shortenerHandle.getHash(), equalTo("fun"));

        URI anotherShortenedUri = new URI("http://www.example2.org");
        shortenerHandle = shortenerService.shorten(anotherShortenedUri);
        MatcherAssert.assertThat(shortenerHandle.getHash(), equalTo("eloquence"));
    }

    @Test
    public void shouldUsePersistenceToLookUpHash() throws URISyntaxException, IOException {
        redisTemplate.opsForValue().set("$count", "0");
        MatcherAssert.assertThat(shortenerService.getShortenedCount(), equalTo(0L));

        redisTemplate.opsForValue().set("$count", "1");
        ShortenerHandle shortenerHandleForWater = new ShortenerHandle();
        String value = new ObjectMapper().writeValueAsString(shortenerHandleForWater);

        redisTemplate.opsForValue().set("water", value);

        ShortenerHandle shortenerHandle = shortenerService.expand("water");
        MatcherAssert.assertThat(shortenerHandle, equalTo(shortenerHandleForWater));
    }

}
