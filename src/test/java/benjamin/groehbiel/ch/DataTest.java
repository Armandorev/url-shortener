package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.ShortenerService;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public abstract class DataTest {

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private RedisManager redisManager;

    @Before
    public void setup() throws IOException {
        dictionaryManager.clear();
        redisManager.clear();

        dictionaryManager.fill(WordNetHelper.loadDirectory("WordNet"));
    }

    @After
    public void flushTable() {
        dictionaryManager.clear();
        redisManager.clear();
    }

}
