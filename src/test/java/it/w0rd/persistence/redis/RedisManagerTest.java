package it.w0rd.persistence.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.w0rd.Application;
import it.w0rd.DataTest;
import it.w0rd.api.ShortenedUrl;
import it.w0rd.ApplicationInitializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = ApplicationInitializer.class)
@WebAppConfiguration
public class RedisManagerTest extends DataTest {

    @Autowired
    private RedisManager redisManager;

    private JedisPool jedisPool;

    @Before
    public void setup() {
        redisManager.clear();

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(jedisPoolConfig, System.getProperty("redis.host"), Integer.parseInt(System.getProperty("redis.port")));
    }

    @Test
    public void checkShortenedUrlIsPersistedAccordingly() throws URISyntaxException, JsonProcessingException {
        ShortenedUrl url = new ShortenedUrl(new URI("http://w0rd.it"), "water", "description...");
        redisManager.storeHash(url);

        try (Jedis jedis = jedisPool.getResource()){
            assertThat(jedis.get(RedisManager.HASH_PREFIX + "water"), not(nullValue()));
            assertThat(jedis.get("http://w0rd.it"), not(nullValue()));
            assertThat(jedis.get(RedisManager.COUNT_FIELD), equalTo("1"));
        }
    }

}