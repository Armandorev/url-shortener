package benjamin.groehbiel.ch.shortener;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShortenerService {

    public static final String SHORTENER_HOST = "http://www.shortener.com/";
    private Long nextUniqueId = 0L;

    Map<URI, Long> hasShortenedURI = new HashMap<>();

    URLShortener urlShortener = new URLShortener();

    public URI shorten(URI inputUri) throws URISyntaxException {
        if (hasShortenedURI.containsKey(inputUri)) {
            Long id = hasShortenedURI.get(inputUri);
            String hash = urlShortener.decode(id);
            return createURI(hash);
        }

        String hash = urlShortener.decode(nextUniqueId);
        URI shortenedURI = createURI(hash);

        hasShortenedURI.put(inputUri, nextUniqueId);
        nextUniqueId++;

        return shortenedURI;
    }

    private URI createURI(String hash) throws URISyntaxException {
        return new URI(SHORTENER_HOST + hash);
    }

    // TODO: runs in O(n), should be O(1). Use Synced HashMaps, O(N) memory, O(1) complexity.
    public URI expand(String hash) throws URISyntaxException {
        Long hashId = urlShortener.encode(hash);
        Map.Entry<URI, Long> existingURI = hasShortenedURI.entrySet().stream().filter(uriLongEntry -> {
            if (uriLongEntry.getValue() == hashId) return true;
            return false;
        }).findFirst().get();

        return existingURI.getKey();
    }

    public Map<URI, Long> getShortenedURIs() {
        return hasShortenedURI;
    }

    // TODO: only exists for tests to clear state after each test. Find better pattern.
    public void clear() {
        nextUniqueId = 0L;
        hasShortenedURI.clear();
    }
}
