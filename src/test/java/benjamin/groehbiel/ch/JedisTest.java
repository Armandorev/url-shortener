package benjamin.groehbiel.ch;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
    public void addEntry() {
        Long index = redisTemplate.opsForList().rightPush("key", "value");
        assertThat(index, equalTo(1L));
    }

}
