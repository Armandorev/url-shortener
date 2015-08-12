package benjamin.groehbiel.ch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@SpringBootApplication
public class ApplicationTest {

    @Bean public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory redis=new JedisConnectionFactory();
        redis.setHostName("localhost");
        redis.setPort(6379);
        redis.setPassword("");
        return redis;
    }

}
