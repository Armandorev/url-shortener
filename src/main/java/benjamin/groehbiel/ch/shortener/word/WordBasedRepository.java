package benjamin.groehbiel.ch.shortener.word;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class WordBasedRepository implements ShortenerRepository {

    @Autowired
    private EnglishDictionary words;

    @Value("${app.domain}")
    public String SHORTENER_HOST;

    private Map<String, URI> resolver = new HashMap<>();
    private Map<URI, ShortenerHandle> originals = new HashMap<>();
    private Long counter = 0L;

    public ShortenerHandle add(URI originalURI) throws URISyntaxException {
        if (originals.containsKey(originalURI)) {
            return originals.get(originalURI);
        } else {
            WordDefinition nextWord = words.get();

            String word = nextWord.getWord();
            String desc = nextWord.getDescription();
            ShortenerHandle shortenerHandle = new ShortenerHandle(originalURI, new URI(SHORTENER_HOST + word), word, desc, counter++);
            resolver.put(word, shortenerHandle.getOriginalURI());
            originals.put(originalURI, shortenerHandle);

            return shortenerHandle;
        }
    }

    public URI get(String hash) {
        return resolver.get(hash);
    }

    public Map<URI, ShortenerHandle> get() {
        return originals;
    }

    public Integer getCount() {
        return resolver.size();
    }

    public void clear() {
        resolver.clear();
        counter = 0L;
    }

}
