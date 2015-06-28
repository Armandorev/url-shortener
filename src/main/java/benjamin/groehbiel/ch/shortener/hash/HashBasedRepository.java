package benjamin.groehbiel.ch.shortener.hash;

import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import benjamin.groehbiel.ch.shortener.ShortenerRepository;
import benjamin.groehbiel.ch.shortener.ShortenerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class HashBasedRepository implements ShortenerRepository {


    @Value("${app.domain}")
    public String SHORTENER_HOST;

    /**
     * Contains a dictionary of the original url as key, and the generated hash as value.
     */
    private Map<URI, ShortenerHandle> originals = new HashMap<>();

    /**
     * Contains a dictionary of the hash of the shortenend url to the original url; used for resolving the URI.
     */
    private Map<String, URI> resolver = new HashMap<>();

    /**
     * Each URL is assigned a unique ID originating from this counter.
     */
    private Long nextUniqueId = 0L;

    private HashBasedURLShortener hashBasedUrlShortener = new HashBasedURLShortener();

    public ShortenerHandle add(URI originalURI) throws URISyntaxException {

        if (originals.containsKey(originalURI)) {
            ShortenerHandle shortenerHandle = originals.get(originalURI);
            return shortenerHandle;
        }

        String hash = hashBasedUrlShortener.decode(nextUniqueId);
        URI shortenedURI = new URI(SHORTENER_HOST + hash);

        ShortenerHandle newShortenerHandle = new ShortenerHandle(originalURI, shortenedURI, hash, "", nextUniqueId);
        originals.put(originalURI, newShortenerHandle);
        resolver.put(hash, originalURI);

        nextUniqueId++;

        return newShortenerHandle;
    }

    public URI get(String hash) {
        return resolver.get(hash);
    }

    public Map<URI,ShortenerHandle> get() {
        return originals;
    }

    public Integer getCount() {
        return originals.size();
    }

    public void clear() {
        resolver.clear();
        originals.clear();
        nextUniqueId = 0L;
    }

}
