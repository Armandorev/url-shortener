package benjamin.groehbiel.ch.shortener.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class DictionaryManager {

    @Autowired
    IDictionaryRepository repository;

    public void clear() {
        repository.deleteAll();
    }

    public void clearUnused() {
        repository.deleteByAvailable(true);
    }

    public long size() {
        return repository.count();
    }

    public Iterable<DictionaryHash> fill(List<DictionaryHash> dictionaryHashes) {
        List<DictionaryHash> savedHashes = new ArrayList<>();

        for (DictionaryHash dictionaryHash : dictionaryHashes) {
            if (!repository.exists(dictionaryHash.getHash())) {
                DictionaryHash savedDictionaryHash = repository.save(dictionaryHash);
                savedHashes.add(savedDictionaryHash);
            }
        }

        return savedHashes;
    }

    public Iterable<DictionaryHash> fill(List<DictionaryHash> dictionaryHashes, Integer size) {
        Collections.shuffle(dictionaryHashes);
        return fill(dictionaryHashes.subList(0, size));
    }

    public DictionaryHash nextHash() {
        DictionaryHash nextWord = repository.findFirst1ByAvailable(true);
        reserveHash(nextWord);
        return nextWord;
    }

    public Long getWordsAvailableSize() {
        return repository.countByAvailable(true);
    }

    private void reserveHash(DictionaryHash nextWord) {
        nextWord.setAvailable(false);
        repository.save(nextWord);
    }

    public Iterable<DictionaryHash> getWords(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Integer insertWords(Integer numberOfWords, List<DictionaryHash> words) {
        int counter = 0;

        for (DictionaryHash dictionaryHash : words) {
            if (!repository.exists(dictionaryHash.getHash())) {
                repository.save(dictionaryHash);
                counter++;
            }

            if (counter == numberOfWords) return counter;
        }

        return counter;
    }
}
