package benjamin.groehbiel.ch.shortener.db;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface IDictionaryRepository extends PagingAndSortingRepository<DictionaryHash, String> {

    DictionaryHash findFirst1ByAvailable(boolean b);

    Long countByAvailable(boolean b);
}