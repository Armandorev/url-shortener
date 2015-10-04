package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.wordnet.WordDefinition;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.List;

public class DatabaseTest {

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private StringRedisTemplate redis;

    @Before
    public void populateTable() throws IOException {
        List<WordDefinition> words = WordNetHelper.load("src/test/resources/WordNet/");
        dictionaryManager.fill(WordNetHelper.turnIntoDictionaryHashes(words));
    }

    @After
    public void flushTable() {
        dictionaryManager.clear();
        redis.getConnectionFactory().getConnection().flushDb();
    }

}
