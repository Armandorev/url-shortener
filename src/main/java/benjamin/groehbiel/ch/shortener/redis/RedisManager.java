package benjamin.groehbiel.ch.shortener.redis;

import benjamin.groehbiel.ch.JsonHelper;
import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

@Service
public class RedisManager {

    public static final String HASH_PREFIX = "hash:";
    public static final String COUNT_FIELD = "$count";

    private Jedis jedis;

    public RedisManager() {
        jedis = new Jedis(System.getProperty("redis.host"), Integer.parseInt(System.getProperty("redis.port")));
        if (!System.getProperty("redis.password").isEmpty()) {
            jedis.auth(System.getProperty("redis.password"));
        }
        jedis.connect();
    }

    public String getHashFor(String key) {
        try {
            return jedis.get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setUrlAndHash(String key, String value) {
        jedis.set(key, value);
    }

    public ShortenerHandle getHandleFor(String hash) throws IOException {
        hash = hash.replace(HASH_PREFIX, "");
        String json = jedis.get(HASH_PREFIX + hash);
        return JsonHelper.unserialize(json);
    }

    public void storeHash(ShortenerHandle shortenerHandle) throws JsonProcessingException {
        URI url = shortenerHandle.getOriginalURI();
        setHashAndHandle(shortenerHandle.getHash(), shortenerHandle);
        setUrlAndHash(url.toString(), shortenerHandle.getHash());
        incrementByOne(COUNT_FIELD);
    }

    public void setHashAndHandle(String hash, ShortenerHandle value) throws JsonProcessingException {
        jedis.set(HASH_PREFIX + hash, JsonHelper.serialize(value));
    }

    public Set<String> getHashes() {
        return getValuesFor(HASH_PREFIX + "*");
    }

    public Long incrementByOne(String key) {
        return jedis.incrBy(key, 1);
    }

    public Long getHashCount() {
        String shortenedSoFar = jedis.get(COUNT_FIELD);
        if (shortenedSoFar == null) {
            return 0L;
        } else {
            return Long.parseLong(shortenedSoFar);
        }
    }

    private Set<String> getValuesFor(String regex) {
        return jedis.keys(regex);
    }

    public void clear() {
        jedis.flushDB();
    }

    public void close() {
        jedis.close();
    }

    public void removeHash(String hashToDelete) throws IOException {
        ShortenerHandle hashHandle = getHandleFor(hashToDelete);
        URI originalURI = hashHandle.getOriginalURI();

        jedis.del(HASH_PREFIX + hashToDelete);
        jedis.del(originalURI.toString());
        jedis.decrBy(COUNT_FIELD, 1);
    }
}
