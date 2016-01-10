package benjamin.groehbiel.ch;

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
    protected DictionaryManager dictionaryManager;

    @Autowired
    protected RedisManager redisManager;

    @Before
    public void setupData() throws IOException {
        dictionaryManager.clear();
        redisManager.clear();

        dictionaryManager.clearAndFill(WordNetHelper.loadDirectory("WordNet"));
    }

    @After
    public void flushTable() {
        dictionaryManager.clear();
        redisManager.clear();
    }

}
