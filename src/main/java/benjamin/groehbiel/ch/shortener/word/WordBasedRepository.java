package benjamin.groehbiel.ch.shortener.word;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Repository
public class WordBasedRepository extends ShortenerRepository {

    @Override
    protected ShortenerHandle add(URI originalURI) throws URISyntaxException {
        return new ShortenerHandle(new URI("http://www.google.ch"), new URI("http://short.io"), "fun", 1L);
    }

    @Override
    protected URI get(String hash) {
        return null;
    }

    @Override
    protected Map<URI, ShortenerHandle> get() {
        return null;
    }

    @Override
    protected Integer getCount() {
        return null;
    }

    @Override
    public void clear() {

    }
}
