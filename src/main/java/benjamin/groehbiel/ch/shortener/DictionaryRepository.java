package benjamin.groehbiel.ch.shortener;

import org.springframework.data.repository.CrudRepository;

public interface DictionaryRepository extends CrudRepository<DictionaryHash, Long> {}