package benjamin.groehbiel.ch.shortener.redis;

import benjamin.groehbiel.ch.ApplicationTest;
import benjamin.groehbiel.ch.PersistenceInitializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
public class RedisManagerTest {

    @Autowired
    RedisManager redisManager;

    @Before
    public void wipeAndPopulate() {
        redisManager.clear();
    }

    @After
    public void tearDown() {
        redisManager.clear();
    }

    @Test
    public void shouldSetAndGetKeyValuePair() {
        redisManager.setValue("$key", "0");
        assertThat(redisManager.getValue("$key"), equalTo("0"));
    }

    @Test
    public void shouldReturnNullAsKeyDoesNotExist(){
        assertThat(redisManager.getValue("key"), is(nullValue()));
    }

    @Test
    public void shouldIncrementCounterByOne() {
        redisManager.setValue("$key", "0");
        redisManager.incrementByOne("$key");
        assertThat(redisManager.getValue("$key"), equalTo("1"));
    }

    @Test
    public void shouldGetValuesByRegex() {
        redisManager.setValue("hello", "this is a text");
        redisManager.setValue("hi", "this is another text");
        Set<String> values = redisManager.getValuesFor("*");
        assertThat(values.size(), equalTo(2));
    }

}