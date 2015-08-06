package benjamin.groehbiel.ch;

import io.pivotal.labs.cfenv.CloudFoundryEnvironment;
import io.pivotal.labs.cfenv.CloudFoundryEnvironmentException;
import io.pivotal.labs.cfenv.CloudFoundryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.util.Map;

public class PersistenceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            configure();
        } catch (CloudFoundryEnvironmentException e) {
            throw new IllegalStateException(e);
        }
    }

    private void configure() throws CloudFoundryEnvironmentException {
        CloudFoundryEnvironment environment = new CloudFoundryEnvironment(System::getenv);
        CloudFoundryService redisService = environment.getService("pcf-redis");

        String redisHost = (String) redisService.getCredentials().get("host");
        String redisPassword = (String) redisService.getCredentials().get("password");
        Integer redisPort = (Integer) redisService.getCredentials().get("port");

        assert(jedisConnectionFactory != null);
        System.setProperty("spring.redis.host", redisHost);
        System.setProperty("spring.redis.password", redisPassword);
        System.setProperty("spring.redis.port", redisPort.toString());

//        jedisConnectionFactory.setHostName(redisHost);
//        jedisConnectionFactory.setPassword(redisPassword);
//        jedisConnectionFactory.setPort(redisPort);
//        System.setProperty("spring.datasource.url", "jdbc:postgresql://" + host + "/" + database);
//        System.setProperty("spring.datasource.username", user);
//        System.setProperty("spring.datasource.password", password);
    }
}

