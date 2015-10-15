package benjamin.groehbiel.ch.shortener.redis;

import benjamin.groehbiel.ch.JsonHelper;
import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

@Service
public class RedisManager {

    public static final String HASH_PREFIX = "hash:";
    public static final String COUNT_FIELD = "$count";

    @Autowired
    private StringRedisTemplate redis;

    public void clear() {
        redis.getConnectionFactory().getConnection().flushDb();
    }

    public String getHashFor(String key) {
        return redis.opsForValue().get(key);
    }

    public void setUrlAndHash(String key, String value) {
        redis.opsForValue().set(key, value);
    }

    public ShortenerHandle getHandleFor(String hash) throws IOException {
        hash = hash.replace(HASH_PREFIX, "");
        String json = redis.opsForValue().get(HASH_PREFIX + hash);
        return JsonHelper.unserialize(json);
    }

    public void storeHash(ShortenerHandle shortenerHandle) throws JsonProcessingException {
        URI url = shortenerHandle.getOriginalURI();
        setHashAndHandle(shortenerHandle.getHash(), shortenerHandle);
        setUrlAndHash(url.toString(), shortenerHandle.getHash());
        incrementByOne(COUNT_FIELD);
    }

    public void setHashAndHandle(String hash, ShortenerHandle value) throws JsonProcessingException {
        redis.opsForValue().set(HASH_PREFIX + hash, JsonHelper.serialize(value));
    }

    public Set<String> getHashes() {
        return getValuesFor(HASH_PREFIX + "*");
    }

    public Long incrementByOne(String key) {
        return redis.opsForValue().increment(key, 1);
    }

    public Long getHashCount() {
        String shortenedSoFar = redis.opsForValue().get(COUNT_FIELD);
        if (shortenedSoFar == null) {
            return 0L;
        } else {
            return Long.parseLong(shortenedSoFar);
        }
    }

    private Set<String> getValuesFor(String regex) {
        return redis.keys(regex);
    }
}
