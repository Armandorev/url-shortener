package benjamin.groehbiel.ch.shortener;

import benjamin.groehbiel.ch.JsonHelper;
import benjamin.groehbiel.ch.shortener.db.DictionaryHash;
import benjamin.groehbiel.ch.shortener.db.DictionaryManager;
import benjamin.groehbiel.ch.shortener.redis.RedisManager;
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

    @Autowired
    private DictionaryManager dictionaryManager;

    @Autowired
    private RedisManager redisManager;

    // TODO: use string as url encoding from here onwards.
    public ShortenerHandle shorten(URI originalURI) throws URISyntaxException, IOException {
        String wordHash = redisManager.getValue(originalURI.toString());

        if (wordHash != null) {
            return JsonHelper.unserialize(wordHash);
        } else {
            return createShortenerHandleFor(originalURI);
        }
    }

    private ShortenerHandle createShortenerHandleFor(URI url) throws URISyntaxException, JsonProcessingException {
        DictionaryHash next = dictionaryManager.takeNextAvailableWord();
        String hash = next.getHash();
        String desc = next.getDescription();
        ShortenerHandle shortenerHandle = new ShortenerHandle(url, hash, desc);

        redisManager.setValue(shortenerHandle.getHash(), JsonHelper.serialize(shortenerHandle));
        redisManager.setValue(url.toString(), shortenerHandle.getHash());
        redisManager.incrementByOne("$count");

        return shortenerHandle;
    }

    public ShortenerHandle expand(String hash) throws URISyntaxException, IOException {
        String json = redisManager.getValue(hash);
        return JsonHelper.unserialize(json);
    }

    public Map<URI, ShortenerHandle> getAllUrls() throws IOException {
        Set<String> uriKeys = redisManager.getValuesFor("*:\\/\\/*");

        HashMap<URI, ShortenerHandle> shortenedUris = new HashMap<>();
        for (String uriKey : uriKeys) {
            String hash = redisManager.getValue(uriKey);
            String json = redisManager.getValue(hash);
            ShortenerHandle shortenerHandle = JsonHelper.unserialize(json);
            shortenedUris.put(shortenerHandle.getOriginalURI(), shortenerHandle);
        }

        return shortenedUris;
    }

    public Long getShortenedCount() {
        String shortenedSoFar = redisManager.getValue("$count");
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
        redisManager.clear();
    }
}
