package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ShortenerService {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private StringRedisTemplate redis;

    // TODO: use string as url encoding from here onwards.
    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException, IOException {
        String wordHash = redis.opsForValue().get(originalURI.toString());

        if (wordHash != null) {
            return lookup(wordHash);
        } else {
            return createShortenerHandleFor(originalURI);
        }
    }

    private String serialised(ShortenerHandle shortenerHandle) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(shortenerHandle);
    }

    private ShortenerHandle lookup(String wordHash) throws IOException {
        return OBJECT_MAPPER.readValue(redis.opsForValue().get(wordHash), ShortenerHandle.class);
    }

    private ShortenerHandle createShortenerHandleFor(URI url) throws URISyntaxException, JsonProcessingException {
        DictionaryHash next = dictionaryManager.takeNextAvailableWord();
        String hash = next.getHash();
        String desc = next.getDescription();
        ShortenerHandle shortenerHandle = new ShortenerHandle(url, hash, desc);

        redis.opsForValue().set(shortenerHandle.getHash(), serialised(shortenerHandle));
        redis.opsForValue().set(url.toString(), shortenerHandle.getHash());
        redis.opsForValue().increment("$count", 1);

        return shortenerHandle;
    }

    public ShortenerHandle expand(String hash) throws URISyntaxException, IOException {
        return lookup(hash);
    }

    public Map<URI, ShortenerHandle> getAllUrls() throws IOException {
        Set<String> uriKeys = redis.keys("*:\\/\\/*");

        HashMap<URI, ShortenerHandle> shortenedUris = new HashMap<>();
        for (String uriKey : uriKeys) {
            String hash = redis.opsForValue().get(uriKey);
            ShortenerHandle shortenerHandle = lookup(hash);
            shortenedUris.put(shortenerHandle.getOriginalURI(), shortenerHandle);
        }

        return shortenedUris;
    }

    public Long getShortenedCount() {
        String shortenedSoFar = redis.opsForValue().get("$count");
        if (shortenedSoFar == null) {
            return 0L;
        } else {
            return Long.parseLong(shortenedSoFar);
        }
    }

    public Long getRemainingCount() {
        return dictionaryManager.getWordsAvailableSize();
    }

    public void clear() {
        redis.getConnectionFactory().getConnection().flushDb();
    }
}
