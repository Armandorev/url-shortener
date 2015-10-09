package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordDefinition;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public abstract class TestBase {

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    protected RedisManager redisManager;

    @Before
    public void populateTable() throws IOException {
        dictionaryManager.clear();
        List<WordDefinition> words = WordNetHelper.load("src/test/resources/WordNet/");
        dictionaryManager.fill(WordNetHelper.turnIntoDictionaryHashes(words));
    }

    @After
    public void flushTable() {
        dictionaryManager.clear();
        redisManager.clear();
    }

}
