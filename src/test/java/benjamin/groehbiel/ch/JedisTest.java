package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
public class JedisTest {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @After
    public void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Test
    public void testJedisConnectionFactory() {
        assertNotNull(jedisConnectionFactory);
    }

    @Test
    public void testRedisTemplate() {
        assertNotNull(redisTemplate);
    }

    @Test
    public void addAndGetEntry() {
        redisTemplate.opsForValue().set("key", "value");
        String value = redisTemplate.opsForValue().get("key");
        Assert.assertThat(value, equalTo("value"));
    }

    @Test
    public void redisContains() {
        String lookup = redisTemplate.opsForValue().get("hello");
        Assert.assertThat(lookup, isEmptyOrNullString());
        redisTemplate.opsForValue().set("hello", "wtf");
        lookup = redisTemplate.opsForValue().get("hello");
        Assert.assertThat(lookup, not(nullValue()));
    }

    @Test
    public void serializePOJO() throws URISyntaxException, IOException {
        ShortenerHandle shortenerHandle = new ShortenerHandle(new URI("http://www.google.ch"), new URI("http://s.it/a"), "a", "a letter");
        String serializedPOJO = new ObjectMapper().writeValueAsString(shortenerHandle);
        redisTemplate.opsForValue().set("a", serializedPOJO);

        String retrievedPOJO = redisTemplate.opsForValue().get("a");
        ShortenerHandle deserializedPOJO = new ObjectMapper().readValue(retrievedPOJO, ShortenerHandle.class);

        MatcherAssert.assertThat(deserializedPOJO, equalTo(shortenerHandle));
    }

}
