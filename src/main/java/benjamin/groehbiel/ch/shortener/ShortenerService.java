package benjamin.groehbiel.ch.shortener;

import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ShortenerService {

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
        return new URI("http://www.shortener.com/" + hash);
    }

    // TODO: runs in O(n), should be O(1).
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
}
