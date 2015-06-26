package benjamin.groehbiel.ch.shortener.word;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
public class WordBasedRepository implements ShortenerRepository {

    private List<WordDefinition> words = new LinkedList<>();

    private Map<String, URI> resolver = new HashMap<>();

    private Map<URI, ShortenerHandle> originals = new HashMap<>();

    private Long counter = 0L;

    public WordBasedRepository() {
        words.add(new WordDefinition("fun", "enjoyment, amusement, or light-hearted pleasure."));
        words.add(new WordDefinition("eloquence", "fluent or persuasive speaking or writing."));
        words.add(new WordDefinition("elephant", "a very large plant-eating mammal with a prehensile trunk, long curved ivory tusks, and large ears, native to Africa and southern Asia."));
    }

    public ShortenerHandle add(URI originalURI) throws URISyntaxException {
        System.out.println("running add....");
        if (originals.containsKey(originalURI)) {
            return originals.get(originalURI);
        } else {
            WordDefinition nextWord = words.remove(0);

            String word = nextWord.getWord();
            String desc = nextWord.getDescription();
            ShortenerHandle shortenerHandle = new ShortenerHandle(originalURI, new URI(ShortenerService.SHORTENER_HOST + word), word, desc, counter++);
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
