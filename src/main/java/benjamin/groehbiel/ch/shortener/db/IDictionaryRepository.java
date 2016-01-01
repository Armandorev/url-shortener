package benjamin.groehbiel.ch.shortener.db;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

public interface IDictionaryRepository extends PagingAndSortingRepository<DictionaryHash, String> {

    DictionaryHash findFirst1ByAvailable(boolean b);

    Long countByAvailable(boolean b);

    @Transactional
    Long deleteByAvailable(boolean b);
}