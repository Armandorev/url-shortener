package benjamin.groehbiel.ch.shortener;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShortenerService {

    /**
     * The URL under which the shortener is running.
     */
    public static final String SHORTENER_HOST = "http://www.shortener.com/";

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

    private URLShortener urlShortener = new URLShortener();

    /**
     * Shortens a provided URL.
     * @param originalURI URL wanting to be shortened
     * @throws URISyntaxException
     */
    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException {
        if (originals.containsKey(originalURI)) {
            ShortenerHandle shortenerHandle = originals.get(originalURI);
            return shortenerHandle;
        }

        String hash = urlShortener.decode(nextUniqueId);
        URI shortenedURI = new URI(SHORTENER_HOST + hash);

        ShortenerHandle newShortenerHandle = new ShortenerHandle(originalURI, shortenedURI, hash, nextUniqueId);
        originals.put(originalURI, newShortenerHandle);
        resolver.put(hash, originalURI);

        nextUniqueId++;

        return newShortenerHandle;
    }

    /**
     * Expands a hash into the original URL.
     * @param hash
     * @return the original URI
     * @throws URISyntaxException
     */
    public URI expand(String hash) throws URISyntaxException {
        return resolver.get(hash);
    }

    // TODO: only exists for tests to clear state after each test.
    public void clear() {
        nextUniqueId = 0L;
        originals.clear();
    }

    public Map<URI, ShortenerHandle> getAllUrls() {
        return originals;
    }
}
