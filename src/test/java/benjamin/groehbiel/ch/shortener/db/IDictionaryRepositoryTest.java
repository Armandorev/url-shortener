package benjamin.groehbiel.ch.shortener.db;

import benjamin.groehbiel.ch.Application;
import benjamin.groehbiel.ch.PersistenceInitializer;
import benjamin.groehbiel.ch.shortener.wordnet.WordDefinition;
import benjamin.groehbiel.ch.shortener.wordnet.WordNetHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = PersistenceInitializer.class)
public class IDictionaryRepositoryTest {

    JdbcTemplate jdbcTemplate;

    @Autowired
    IDictionaryRepository IDictionaryRepository;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    @Before
    public void clearDb() {
        IDictionaryRepository.deleteAll();
    }

    @Test
    public void isZeroCountWhenNoInserts() {
        assertThat(IDictionaryRepository.count(), equalTo(0L));
    }

    @Test
    public void insertHash() {
        DictionaryHash dictionaryItem = new DictionaryHash("water", "en", "blablabla", false);
        DictionaryHash newDictionaryItem = IDictionaryRepository.save(dictionaryItem);
        assertThat(IDictionaryRepository.count(), equalTo(1L));
    }

    @Test
    public void addAllHashesToDatabase() throws IOException {
        File wordNet = new ClassPathResource("WordNet").getFile();
        List<WordDefinition> englishWords = WordNetHelper.load(wordNet.getPath());

        List<DictionaryHash> dictionaryHashes = englishWords.stream().map(w -> {
            return new DictionaryHash(w.getWord(), "en", w.getDescription(), false);
        }).collect(toList());

        IDictionaryRepository.save(dictionaryHashes);

        assertThat(IDictionaryRepository.count(), equalTo((long) 21));
    }

}
