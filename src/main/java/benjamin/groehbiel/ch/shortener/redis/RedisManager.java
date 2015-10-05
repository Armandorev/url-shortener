package benjamin.groehbiel.ch.shortener.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Repository
public class RedisManager {

    @Autowired
    private StringRedisTemplate redis;

    public void clear() {
        redis.getConnectionFactory().getConnection().flushDb();
    }

    public void setValue(String key, String value) {
        redis.opsForValue().set(key, value);
    }

    public String getValue(String key) {
        return redis.opsForValue().get(key);
    }

    public Long incrementByOne(String key) {
        return redis.opsForValue().increment(key, 1);
    }

    public Set<String> getValuesFor(String regex) {
        return redis.keys(regex);
    }

}
