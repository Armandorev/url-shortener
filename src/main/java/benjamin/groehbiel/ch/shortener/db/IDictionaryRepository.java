package benjamin.groehbiel.ch.shortener.db;

import org.springframework.data.repository.CrudRepository;

public interface IDictionaryRepository extends CrudRepository<DictionaryHash, Long> {

    DictionaryHash findFirst1ByAvailable(boolean b);

    Long countByAvailable(boolean b);
}