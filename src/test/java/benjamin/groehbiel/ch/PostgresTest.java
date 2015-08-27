package benjamin.groehbiel.ch;

import benjamin.groehbiel.ch.shortener.*;
import org.hamcrest.Matcher;
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
@SpringApplicationConfiguration(classes = ApplicationTest.class, initializers = PersistenceInitializer.class)
public class PostgresTest {

    JdbcTemplate jdbcTemplate;

    @Autowired
    DictionaryRepository dictionaryRepository;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @After
    @Before
    public void clearDb() {
        dictionaryRepository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE dictionary_hash_id_seq RESTART WITH 1;");
    }

    @Test
    public void isZeroCountWhenNoInserts() {
        assertThat(dictionaryRepository.count(), equalTo(0L));
    }

    @Test
    public void insertHash() {
        DictionaryHash dictionaryItem = new DictionaryHash("water", "en", "blablabla", false);
        DictionaryHash newDictionaryItem = dictionaryRepository.save(dictionaryItem);
        assertThat(dictionaryRepository.count(), equalTo(1L));
        assertThat(newDictionaryItem.getHashId(), equalTo(1L));
    }

    @Test
    public void addAllHashesToDatabase() throws IOException {
        File wordNet = new ClassPathResource("WordNet").getFile();
        List<WordDefinition> englishWords = WordNetHelper.load(wordNet.getPath());

        List<DictionaryHash> dictionaryHashes = englishWords.stream().map(w -> {
            return new DictionaryHash(w.getWord(), "en", w.getDescription(), false);
        }).collect(toList());

        dictionaryRepository.save(dictionaryHashes);

        assertThat(dictionaryRepository.count(), equalTo((long) 21));
    }

}
