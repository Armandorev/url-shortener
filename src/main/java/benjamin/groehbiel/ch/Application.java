package benjamin.groehbiel.ch;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .initializers(new PersistenceInitializer())
                .run(args);
    }

    @Bean public RedisConnectionFactory redisConnectionFactory(){
        JedisConnectionFactory redis = new JedisConnectionFactory();
        redis.setHostName(System.getProperty("spring.redis.host"));
        redis.setPort(Integer.parseInt(System.getProperty("spring.redis.port")));
        redis.setPassword(System.getProperty("spring.redis.password"));
        return redis;
    }

}
