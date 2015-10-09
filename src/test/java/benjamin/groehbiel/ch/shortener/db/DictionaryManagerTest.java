package benjamin.groehbiel.ch.shortener.db;

import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.PersistenceInitializer;
import benjamin.groehbiel.ch.shortener.wordnet.WordDefinition;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = PersistenceInitializer.class)
@WebAppConfiguration
public class DictionaryManagerTest {

    @Autowired
    DictionaryManager dictionaryManager;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        dictionaryManager.clear();
    }

    @Test
    public void shouldResetAndFillTable() throws IOException {
        populateTable();
        assertThat(dictionaryManager.size(), equalTo(21L));
        assertThat(dictionaryManager.find(1L).getHashId(), equalTo(1L));
    }

    @Test
    public void shouldClearAllWords() {
        dictionaryManager.clear();
        assertThat(dictionaryManager.size(), equalTo(0L));
    }

    @Test
    public void shouldGetNextAvailableWord() throws IOException {
        populateTable();
        DictionaryHash nextWord = dictionaryManager.takeNextAvailableWord();

        assertThat(nextWord.getHash(), containsString("able"));
        assertThat(nextWord.getHashId(), equalTo(1L));
        assertThat(nextWord.getAvailable(), equalTo(false));
    }

    @Test
    public void shouldReturnCurrentSizeOfAvailableWords() throws IOException {
        populateTable();
        assertThat(dictionaryManager.getWordsAvailableSize(), equalTo(21L));
        dictionaryManager.takeNextAvailableWord();
        dictionaryManager.takeNextAvailableWord();
        assertThat(dictionaryManager.getWordsAvailableSize(), equalTo(19L));
    }

    private void populateTable() throws IOException {
        List<WordDefinition> words = WordNetHelper.load("src/test/resources/WordNet/");
        dictionaryManager.fill(WordNetHelper.turnIntoDictionaryHashes(words));
    }

}
