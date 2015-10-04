package benjamin.groehbiel.ch.shortener.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DictionaryManager {

    @Autowired
    IDictionaryRepository repository;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void clear() {
        repository.deleteAll();
        jdbcTemplate.execute("ALTER SEQUENCE dictionary_hash_id_seq RESTART WITH 1;");
    }

    public long size() {
        return repository.count();
    }

    public Iterable<DictionaryHash> fill(List<DictionaryHash> dictionaryHashs) {
        return repository.save(dictionaryHashs);
    }

    public DictionaryHash find(Long i) {
        return repository.findOne(i);
    }

    public DictionaryHash takeNextAvailableWord() {
        DictionaryHash nextWord = repository.findFirst1ByAvailable(true);
        nextWord.setAvailable(false);
        repository.save(nextWord);
        return nextWord;
    }

    public Long getWordsAvailableSize() {
        return repository.countByAvailable(true);
    }
}
