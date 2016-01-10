package benjamin.groehbiel.ch.shortener.redis;

import benjamin.groehbiel.ch.JsonHelper;
import benjamin.groehbiel.ch.shortener.ShortenerHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

@Service
public class RedisManager {

    public static final String HASH_PREFIX = "hash:";
    public static final String COUNT_FIELD = "$count";

    private JedisPool pool;

    public RedisManager() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(16);

        if (System.getProperty("redis.password").isEmpty()) {
            pool = createJedisPoolForTestEnv(jedisPoolConfig);
        } else {
            pool = createJedisPoolForProdEnv(jedisPoolConfig);
        }
    }

    public String getHashFor(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setUrlAndHash(String key, String value) {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(key, value);
        }
    }

    public ShortenerHandle getHandleFor(String hash) throws IOException {
        hash = hash.replace(HASH_PREFIX, "");

        try (Jedis jedis = pool.getResource()) {
            String json = jedis.get(HASH_PREFIX + hash);
            return JsonHelper.unserialize(json);
        }

    }

    public void storeHash(ShortenerHandle shortenerHandle) throws JsonProcessingException {
        URI url = shortenerHandle.getOriginalURI();
        setHashAndHandle(shortenerHandle.getHash(), shortenerHandle);
        setUrlAndHash(url.toString(), shortenerHandle.getHash());
        incrementByOne(COUNT_FIELD);
    }

    public void setHashAndHandle(String hash, ShortenerHandle value) throws JsonProcessingException {
        try (Jedis jedis = pool.getResource()) {
            jedis.set(HASH_PREFIX + hash, JsonHelper.serialize(value));
        }
    }

    public Set<String> getHashes() {
        return getValuesFor(HASH_PREFIX + "*");
    }

    public Long incrementByOne(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.incrBy(key, 1);
        }
    }

    public Long getHashCount() {
        try (Jedis jedis = pool.getResource()) {
            String shortenedSoFar = jedis.get(COUNT_FIELD);

            if (shortenedSoFar == null) {
                return 0L;
            } else {
                return Long.parseLong(shortenedSoFar);
            }
        }
    }

    private Set<String> getValuesFor(String regex) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.keys(regex);
        }
    }

    public void clear() {
        try (Jedis jedis = pool.getResource()) {
            jedis.flushAll();
        }
    }

    public void removeHash(String hashToDelete) throws IOException {
        ShortenerHandle hashHandle = getHandleFor(hashToDelete);
        URI originalURI = hashHandle.getOriginalURI();

        try (Jedis jedis = pool.getResource()) {
            jedis.del(HASH_PREFIX + hashToDelete);
            jedis.del(originalURI.toString());
            jedis.decrBy(COUNT_FIELD, 1);
        }
    }

    private JedisPool createJedisPoolForProdEnv(JedisPoolConfig jedisPoolConfig) {
        return new JedisPool(jedisPoolConfig, System.getProperty("redis.host"), Integer.parseInt(System.getProperty("redis.port")), 2000, System.getProperty("redis.password"));
    }

    private JedisPool createJedisPoolForTestEnv(JedisPoolConfig jedisPoolConfig) {
        return new JedisPool(jedisPoolConfig, System.getProperty("redis.host"), Integer.parseInt(System.getProperty("redis.port")));
    }

}
