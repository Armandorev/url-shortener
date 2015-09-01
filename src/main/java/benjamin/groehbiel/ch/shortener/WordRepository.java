package benjamin.groehbiel.ch.shortener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class WordRepository {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private EnglishDictionary englishWords;

    @Autowired
    private StringRedisTemplate redis;

    @Value("${app.domain}")
    public String SHORTENER_HOST;

    @Value("${app.protocol}")
    public String SHORTENER_PROTOCOL;

    public ShortenerHandle add(URI originalURI) throws URISyntaxException, IOException {
        String wordHash = redis.opsForValue().get(originalURI.toString());

        if (wordHash != null) {
            ShortenerHandle shortenerHandle = deserializeShortenerHandle(wordHash);
            return shortenerHandle;
        }

        ShortenerHandle shortenerHandle = createShortenerHandleFor(originalURI);
        return shortenerHandle;
    }

    private ShortenerHandle createShortenerHandleFor(URI originalURI) throws URISyntaxException, JsonProcessingException {
        WordDefinition nextWord = englishWords.getNextWord();
        String word = nextWord.getWord();
        String desc = nextWord.getDescription();
        ShortenerHandle shortenerHandle = new ShortenerHandle(originalURI, new URI(SHORTENER_PROTOCOL + "://" + SHORTENER_HOST + "/" + word), word, desc);

        redis.opsForValue().set(shortenerHandle.getHash(), serializeShortenerHandle(shortenerHandle));
        redis.opsForValue().set(shortenerHandle.getOriginalURI().toString(), shortenerHandle.getHash());
        redis.opsForValue().increment("$count", 1);

        return shortenerHandle;
    }

    public ShortenerHandle getShortenerHandleFor(String hash) throws IOException {
        ShortenerHandle shortenerHandle = deserializeShortenerHandle(hash);
        return shortenerHandle;
    }

    public Map<URI, ShortenerHandle> getAllShortenerHandles() throws IOException {
        Set<String> uriKeys = redis.keys("*:\\/\\/*");

        HashMap<URI, ShortenerHandle> shortenedUris = new HashMap<>();
        for (String uriKey : uriKeys) {
            String hash = redis.opsForValue().get(uriKey);
            ShortenerHandle shortenerHandle = getShortenerHandleFor(hash);
            shortenedUris.put(shortenerHandle.getOriginalURI(), shortenerHandle);
        }

        return shortenedUris;
    }

    public Long size() {
        String shortenedSoFar = redis.opsForValue().get("$count");
        if (shortenedSoFar == null) {
            return 0L;
        } else {
            return Long.parseLong(shortenedSoFar);
        }
    }

    public int getRemainingHashCount() {
        return englishWords.size();
    }

    public void clear() {
        redis.getConnectionFactory().getConnection().flushDb();
    }

    private String serializeShortenerHandle(ShortenerHandle shortenerHandle) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(shortenerHandle);
    }

    private ShortenerHandle deserializeShortenerHandle(String wordHash) throws IOException {
        return OBJECT_MAPPER.readValue(redis.opsForValue().get(wordHash), ShortenerHandle.class);
    }

}
